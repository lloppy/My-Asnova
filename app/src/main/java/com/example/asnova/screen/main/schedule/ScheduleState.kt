package com.example.asnova.screen.main.schedule

import java.time.LocalDate
import java.time.LocalTime


data class ScheduleState(
    var value: List<Schedule> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)

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

data class ScheduleTask(
    val id: String = "",
    val accessCode: String = "",
    val nameApp: String = "",
    val link: String = ""
)