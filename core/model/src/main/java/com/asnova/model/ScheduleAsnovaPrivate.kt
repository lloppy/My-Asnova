package com.asnova.model

import java.time.LocalDateTime

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
