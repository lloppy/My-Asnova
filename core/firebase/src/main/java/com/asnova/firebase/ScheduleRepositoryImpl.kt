package com.asnova.firebase

import android.util.Log
import com.asnova.domain.repository.firebase.CalendarService
import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.asnova.model.Schedule
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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
    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _databaseReference: CollectionReference = _database.collection("someNode")

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

    override fun getAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        getPrivateSchedule { resource ->
            when (resource) {
                is Resource.Loading -> {
                    callback(Resource.Loading())
                }

                is Resource.Success -> {
                    val schedules = resource.data

                    val uniqueClasses = schedules
                        ?.mapNotNull { it.trimmedSummary }
                        ?.filter { className ->
                            (className.count { char -> char == '"' } >= 2
                                    || className.contains("Обучение", ignoreCase = true))
                                    && !className.contains("выездное", ignoreCase = true)
                                    && !className.contains("выезное", ignoreCase = true)
                                    && !className.contains("экзамен", ignoreCase = true)
                                    && !className.contains(
                                "Организационное собрание",
                                ignoreCase = true
                            )
                                    && !className.contains("Орг.собрание", ignoreCase = true)
                                    && !className.contains("Орг. собрание", ignoreCase = true)
                        }
                        ?.distinct()

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

    private fun parseScheduleFromHtml(html: String, year: Int): List<ScheduleAsnovaSite> {
        val document = Jsoup.parse(html)
        val scheduleElements = document.select("div.seocategory__prodblock")
        Log.d("calendar_site_info", scheduleElements.text())

        return scheduleElements.mapNotNull { element ->
            val linkElement =
                element.selectFirst(".seocategory__prodblock-link") ?: return@mapNotNull null
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