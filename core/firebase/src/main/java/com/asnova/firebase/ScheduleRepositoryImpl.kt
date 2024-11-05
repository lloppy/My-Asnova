package com.asnova.firebase

import android.util.Log
import com.asnova.domain.repository.firebase.CalendarService
import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.asnova.model.Schedule
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDate
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val calendarService: CalendarService
) : ScheduleRepository {
    private val _database: DatabaseReference = Firebase.database.reference
    private val excludedWordsRef = _database.child("excludedWords")
    private val asnovaClassesRef = _database.child("asnovaClasses")

    override fun clearAsnovaClasses(callback: (Boolean) -> Unit) {
        asnovaClassesRef.removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    override fun getPrivateSchedule(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit) {
        callback(Resource.Loading())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val scheduleList = calendarService.getPrivateSchedule()

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

    override fun getRawAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        getPrivateSchedule { resource ->
            when (resource) {
                is Resource.Loading -> {
                    callback(Resource.Loading())
                }

                is Resource.Success -> {
                    val schedules = resource.data
                    Log.d("getAsnovaClasses","до " + resource.data?.size.toString())

                    excludedWordsRef.get().addOnSuccessListener { snapshot ->
                        val includeWords = mutableListOf<String>()
                        val excludeWords = mutableListOf<String>()

                        snapshot.children.forEach { doc ->
                            val word = doc.child("word").getValue(String::class.java)
                            val include = doc.child("include").getValue(Boolean::class.java)

                            if (word != null && include != null) {
                                if (include) {
                                    includeWords.add(word)
                                } else {
                                    excludeWords.add(word)
                                }
                            }
                        }
                        Log.i("excludedWords", "Include words: $includeWords")
                        Log.i("excludedWords", "Exclude words: $excludeWords")

                        val uniqueClasses = schedules
                            ?.mapNotNull { it.summary }
                            ?.filter { className ->
                                (className.count { char -> char == '"' } >= 2
                                        || className.contains("Обучение", ignoreCase = true))
                                        && excludeWords.none { word ->
                                    className.contains(word, ignoreCase = true)
                                }
                            }
                            ?.distinct()?.toSet()
                        Log.d("getAsnovaClasses","после " + resource.data?.size.toString())

                        val asnovaClasses = uniqueClasses?.map { className ->
                            AsnovaStudentsClass(name = className)
                        }?.toSet()?.toList()
                        Log.d("getAsnovaClasses","после asnovaClasses " + asnovaClasses?.size.toString())

                        callback(Resource.Success(asnovaClasses?.toList()))


                    }.addOnFailureListener {
                        Log.e("excludedWords", "Error getting data", it)
                        callback(Resource.Error(it.message ?: "Unknown error"))
                    }
                }

                is Resource.Error -> {
                    callback(Resource.Error(resource.message ?: "Unknown error"))
                }
            }
        }
    }

    override fun getAsnovaClassesFromFirebase(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        callback(Resource.Loading())

            asnovaClassesRef.get().addOnSuccessListener { snapshot ->

                val asnovaClasses = snapshot.children.mapNotNull { childSnapshot ->
                    try{
                        childSnapshot.getValue(AsnovaStudentsClass::class.java)
                    }catch(e:Exception){
                        Log.e("FirebaseError","Error converting class:${e.message}")
                        null
                    }
                }

                callback(Resource.Success(asnovaClasses))
        }.addOnFailureListener { exception ->
            Log.e("FirebaseError", "Error getting classes from Firebase: ${exception.message}")
            callback(Resource.Error(exception.message ?: "Unknown error"))
        }
    }

    override fun pushAsnovaClasses(
        asnovaClasses: List<AsnovaStudentsClass>, callback: (Resource<Boolean>) -> Unit
    ) {
        callback(Resource.Loading())
        try {
            asnovaClasses.forEach { asnovaClass ->
                asnovaClassesRef.push().setValue(asnovaClass)
            }
            callback(Resource.Success(data = true))

        } catch (e: Exception) {
            callback(Resource.Error(e.message.toString()))
        }
    }

    private fun extractYearFromDocument(document: Document): Int {
        val yearText = document.selectFirst("div.seocategory__prodblock-title__inner")?.text()
        return yearText?.split(" ")?.last()?.toIntOrNull() ?: LocalDate.now().year
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
}