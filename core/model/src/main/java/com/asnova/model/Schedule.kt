package com.asnova.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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

//    val location: String?,
//    val description: String?,
//    val status: String?,
//    val organizer: String?,
//    val attendees: List<String>?,
)