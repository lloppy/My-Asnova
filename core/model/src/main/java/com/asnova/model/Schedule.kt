package com.asnova.model

import java.time.LocalDateTime

// Паттерн Static factory method
class Schedule {
    companion object {
        fun createPrivateSchedule(uid: String, summary: String?, created: LocalDateTime, start: LocalDateTime, end: LocalDateTime): ScheduleAsnovaPrivate {
            return ScheduleAsnovaPrivate(uid, summary, created, start, end)
        }

        fun createSiteSchedule(dateRange: String, year: Int, timeRange: String, description: String, imageUrl: String, newsLink: String): ScheduleAsnovaSite {
            return ScheduleAsnovaSite(dateRange, year, timeRange, description, imageUrl, newsLink)
        }
    }
}

data class ScheduleAsnovaPrivate(
    val uid: String,
    val summary: String?,
    val created: LocalDateTime,
    val start: LocalDateTime,
    val end: LocalDateTime,
) {
    val trimmedSummary: String
        get() {
            val text = summary ?: ""
            val regex = "\\d+\\s*(.*)".toRegex()
            val matchResult = regex.find(text)
            return matchResult?.groups?.get(1)?.value?.trim() ?: text
        }

    val classroomNumber: String
        get() {
            val text = summary ?: ""
            val regex = "(\\d+)\\s*".toRegex()
            val matchResult = regex.find(text)
            return matchResult?.groups?.get(1)?.value?.trim() ?: ""
        }

    val startTime: String
        get() {
            return start.toString().takeLast(5)
        }

    val endTime: String
        get() {
            return end.toString().takeLast(5)
        }
}

data class ScheduleAsnovaSite(
    val dateRange: String,
    val year: Int,
    val timeRange: String,
    val description: String,
    val imageUrl: String,
    val newsLink: String
)