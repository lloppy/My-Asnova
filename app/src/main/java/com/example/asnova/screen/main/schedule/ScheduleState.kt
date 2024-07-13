package com.example.asnova.screen.main.schedule

import com.asnova.model.AsnovaSchedule
import java.time.LocalDate
import java.time.LocalTime
import com.asnova.model.Schedule


data class ScheduleState(
    var value: List<Schedule> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)

data class AsnovaScheduleState(
    var value: List<AsnovaSchedule> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)

data class ScheduleTask(
    val id: String = "",
    val accessCode: String = "",
    val nameApp: String = "",
    val link: String = ""
)