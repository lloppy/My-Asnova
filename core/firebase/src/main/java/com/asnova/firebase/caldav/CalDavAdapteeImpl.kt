package com.asnova.firebase.caldav

import com.asnova.model.CalDavConfig
import com.asnova.model.Schedule
import com.asnova.model.ScheduleAsnovaPrivate
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Property
import net.fortuna.ical4j.model.component.VEvent
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.StringReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class CalDavAdapteeImpl() : CalDavAdaptee {
    private val client = OkHttpClient()

    override fun getPrivateScheduleFromCalDav(config: CalDavConfig): List<ScheduleAsnovaPrivate> {
        val calendarData = fetchCalendarData(config)
        val calendar = calendarData?.let { parseCalendarData(it) } ?: return emptyList()

        val events: List<VEvent> = calendar.components.filterIsInstance<VEvent>()
        return events.map { event ->
            Schedule.createPrivateSchedule(
                summary = event.getProperty<Property>(Property.SUMMARY)?.value,
                created = parseDate(event.getProperty<Property>(Property.CREATED)?.value),
                start = parseDate(event.getProperty<Property>(Property.DTSTART)?.value),
                end = parseDate(event.getProperty<Property>(Property.DTEND)?.value),
                uid = event.getProperty<Property>(Property.UID)?.value.toString()
            )
        }
    }

    override fun getPrivateScheduleMapFromCalDav(config: CalDavConfig): Map<LocalDate, List<ScheduleAsnovaPrivate>> {
        val calendarData = fetchCalendarData(config)
        val calendar = calendarData?.let { parseCalendarData(it) } ?: return emptyMap()

        val events: List<VEvent> = calendar.components.filterIsInstance<VEvent>()
        val scheduleMap = mutableMapOf<LocalDate, MutableList<ScheduleAsnovaPrivate>>()

        events.forEach { event ->
            val schedule = Schedule.createPrivateSchedule(
                summary = event.getProperty<Property>(Property.SUMMARY)?.value,
                created = parseDate(event.getProperty<Property>(Property.CREATED)?.value),
                start = parseDate(event.getProperty<Property>(Property.DTSTART)?.value),
                end = parseDate(event.getProperty<Property>(Property.DTEND)?.value),
                uid = event.getProperty<Property>(Property.UID)?.value.toString()
            )

            val startDate = schedule.start.toLocalDate()
            scheduleMap.getOrPut(startDate) { mutableListOf() }.add(schedule)
        }
        return scheduleMap.mapValues { entry ->
            entry.value.sortedBy { it.start }
        }
    }

    private fun parseDate(dateStr: String?): LocalDateTime {
        val createdFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
        val fallbackDate = LocalDateTime.of(2004, 4, 17, 0, 0)

        if (dateStr.isNullOrEmpty()) return fallbackDate

        return try {
            when {
                dateStr.endsWith("Z") -> LocalDateTime.parse(dateStr, createdFormatter)
                    .atOffset(ZoneOffset.UTC).toLocalDateTime()

                else -> {
                    val datePart = dateStr.substring(0, 8)
                    val timePart = dateStr.substring(9)
                    val date = LocalDate.parse(datePart, DateTimeFormatter.BASIC_ISO_DATE)
                    val time = LocalTime.parse(timePart, DateTimeFormatter.ofPattern("HHmmss"))
                    LocalDateTime.of(date, time)
                }
            }
        } catch (e: Exception) {
            fallbackDate
        }
    }

    private fun fetchCalendarData(config: CalDavConfig): String? {
        val request = Request.Builder()
            .url(config.baseUrl)
            .header("Authorization", Credentials.basic(config.username, config.password))
            .build()

        client.newCall(request).execute().use { response ->
            return if (!response.isSuccessful) null else response.body?.string()
        }
    }

    private fun parseCalendarData(data: String): Calendar {
        val stringReader = StringReader(data)
        return CalendarBuilder().build(stringReader)
    }
}
