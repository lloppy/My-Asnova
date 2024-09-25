package com.asnova.model

import java.time.LocalDate
import java.time.LocalTime

data class ScheduleFirebase(
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
