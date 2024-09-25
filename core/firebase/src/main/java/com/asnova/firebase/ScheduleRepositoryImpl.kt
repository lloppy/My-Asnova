package com.asnova.firebase

import CalDavClient
import android.util.Log
import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import com.asnova.model.Resource
import com.asnova.model.Schedule
import com.asnova.model.ScheduleFirebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val calDavClient: CalDavClient
) : ScheduleRepository {
    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _databaseReference: CollectionReference = _database.collection("schedule")
    override fun addNewLesson(scheduleFirebase: ScheduleFirebase, callback: (Resource<Boolean>) -> Unit) {
        callback(Resource.Loading())
        val id = _databaseReference.document().id

        val lesson = com.asnova.firebase.model.Schedule(
            id = id,
            date = Timestamp(
                Date.from(
                    scheduleFirebase.date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                )
            ),
            start = Timestamp(
                Date.from(
                    LocalDate.now().atTime(scheduleFirebase.start).atZone(ZoneId.systemDefault())
                        .toInstant()
                )
            ),
            end = Timestamp(
                Date.from(
                    LocalDate.now().atTime(scheduleFirebase.end).atZone(ZoneId.systemDefault()).toInstant()
                )
            ),
            lesson = scheduleFirebase.lesson,
            status = scheduleFirebase.status,
            classRoom = scheduleFirebase.classRoom,
            teacher = scheduleFirebase.teacher,
            grade = scheduleFirebase.grade,
            task = scheduleFirebase.task,
            homeWork = scheduleFirebase.homeWork
        )
        _databaseReference.document(id).set(lesson).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(Resource.Success(true))
            } else {
                callback(Resource.Success(false))
            }
        }.addOnFailureListener {
            callback(Resource.Error(it.message.toString()))
        }
    }

    override fun getAllScheduleFromFirebase(callback: (Resource<List<ScheduleFirebase>>) -> Unit) {
        callback(Resource.Loading())
        _databaseReference.get().addOnSuccessListener { snapshot ->
            if (!snapshot.isEmpty) {
                val scheduleFirebaseList = mutableListOf<ScheduleFirebase>()
                snapshot.forEach {
                    val temp = it.toObject(com.asnova.firebase.model.Schedule::class.java)
                    val scheduleFirebase = ScheduleFirebase(
                        id = temp.id,
                        date = LocalDateTime.ofInstant(
                            temp.date.toDate().toInstant(),
                            ZoneId.systemDefault()
                        ).toLocalDate(),
                        start = LocalDateTime.ofInstant(
                            temp.start.toDate().toInstant(),
                            ZoneId.systemDefault()
                        ).toLocalTime(),
                        end = LocalDateTime.ofInstant(
                            temp.end.toDate().toInstant(),
                            ZoneId.systemDefault()
                        ).toLocalTime(),
                        lesson = temp.lesson,
                        status = temp.status,
                        classRoom = temp.classRoom,
                        teacher = temp.teacher,
                        grade = temp.grade,
                        task = temp.task,
                        homeWork = temp.homeWork
                    )
                    scheduleFirebaseList.add(scheduleFirebase)
                }
                Log.d("calendar_info", "Success: ${scheduleFirebaseList.size} items")
                callback(Resource.Success(scheduleFirebaseList))
            } else {
                Log.d("calendar_info", "Success: Empty list")
                callback(Resource.Success(emptyList()))
            }
        }.addOnFailureListener {
            Log.d("calendar_info", "Error: ${it.message}")
            callback(Resource.Error(it.message.toString()))
        }
    }

    override fun getScheduleFromCalDav(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit) {
        callback(Resource.Loading())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val scheduleList = calDavClient.getScheduleList()

                scheduleList.forEach {
                    Log.d("calendar_info", it.summary.toString())
                }

                withContext(Dispatchers.Main) {
                    callback(Resource.Success(scheduleList))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    override fun getAllSchedule(callback: (Resource<List<ScheduleFirebase>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getScheduleFromSite(callback: (Resource<List<ScheduleAsnovaSite>>) -> Unit) {
        callback(Resource.Loading())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val document = Jsoup.connect("https://asnova.pro/raspisanie").get()
                val year = extractYearFromDocument(document)
                val scheduleList = parseScheduleFromHtml(document.html(), year)

                withContext(Dispatchers.Main) {
                    callback(Resource.Success(scheduleList))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(Resource.Error(e.message.toString()))
                }
            }
        }
    }

    override fun getAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        getScheduleFromCalDav { resource ->
            when (resource) {
                is Resource.Loading -> {
                    callback(Resource.Loading())
                }
                is Resource.Success -> {
                    val schedules = resource.data

                    val uniqueClasses = schedules
                        ?.mapNotNull { it.trimmedSummary }
                        ?.filter { className ->
                            className.count { char -> char == '"' } >= 2
                                    || className.contains("Обучение")
                        }
                        ?.toSet()
                        ?.toList()

                    val asnovaClasses = uniqueClasses?.map { className ->
                        AsnovaStudentsClass(name = className)
                    }

                    callback(Resource.Success(asnovaClasses))
                }
                is Resource.Error -> {
                    callback(Resource.Error(resource.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun extractYearFromDocument(document: Document): Int {
        val yearText = document.selectFirst("div.seocategory__prodblock-title__inner")?.text()
        return yearText?.split(" ")?.last()?.toIntOrNull() ?: LocalDate.now().year
    }
}

private fun parseScheduleFromHtml(html: String, year: Int): List<ScheduleAsnovaSite> {
    val document = Jsoup.parse(html)
    val scheduleElements = document.select("div.seocategory__prodblock")
    Log.d("calendar_site_info", scheduleElements.text())

    return scheduleElements.mapNotNull { element ->
        val linkElement = element.selectFirst(".seocategory__prodblock-link") ?: return@mapNotNull null
        val dateAndTimeText = linkElement.text().split(", ")
        Log.d("calendar_site_info", "text $dateAndTimeText")

        if (dateAndTimeText.size < 3) return@mapNotNull null

        val dateRange = dateAndTimeText[0]
        val timeRange = dateAndTimeText[1]
        val description = dateAndTimeText[2]

        val imageUrlElement = element.selectFirst(".seocategory__prodblock-img img")
        val imageUrl = imageUrlElement?.attr("src") ?: ""

        val newsLinkElement = element.selectFirst(".seocategory__prodblock-link")
        val newsLink = newsLinkElement?.attr("href") ?: ""

        // Паттерн Static factory method
        Schedule.createSiteSchedule(
            dateRange = dateRange,
            year = year,
            timeRange = timeRange,
            description = description,
            imageUrl = "https://asnova.pro$imageUrl",
            newsLink = newsLink
        )
    }
}
