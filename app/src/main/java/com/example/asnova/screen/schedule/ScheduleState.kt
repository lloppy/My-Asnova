package com.example.asnova.screen.schedule

import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite
import java.time.LocalDate

data class ScheduleState(
    //var privateSchedule: List<ScheduleAsnovaPrivate> = emptyList(),
    var privateSchedule: Map<LocalDate, List<ScheduleAsnovaPrivate>> = mutableMapOf(),
    var siteSchedule: List<ScheduleAsnovaSite> = emptyList(),
    val error: String = "",
    val loading: Boolean = false,
    var currentScheduleIsPrivate: Boolean = true
)
