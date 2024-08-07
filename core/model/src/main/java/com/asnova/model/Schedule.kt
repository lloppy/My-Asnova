package com.asnova.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year

data class Schedule(
    val id: String = "",
    val date: LocalDate = LocalDate.now(),
    val start: LocalTime = LocalTime.now(),
    val end: LocalTime = LocalTime.now(),
    val status: Int = 0,
    val classRoom: String = "",
    val lesson: String = "",
    val teacher: String = "",
    val grade: Int = 1,
    val task: ScheduleTask = ScheduleTask(),
    val homeWork: List<String> = emptyList()
)

data class AsnovaSchedule(
    val uid: String,
    val summary: String?,
    val created: LocalDateTime,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val task: ScheduleTask = ScheduleTask(),
    val grade: Int = 1,
    val homeWork: List<String> = emptyList()
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


data class AsnovaSiteSchedule(
    val dateRange: String,
    val year: Int,
    val timeRange: String,
    val description: String,
    val imageUrl: String,
    val newsLink: String
)