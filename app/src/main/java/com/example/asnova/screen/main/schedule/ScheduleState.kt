package com.example.asnova.screen.main.schedule

import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.ScheduleAsnovaSite

data class ScheduleState(
    var privateSchedule: List<ScheduleAsnovaPrivate> = emptyList(),
    var siteSchedule: List<ScheduleAsnovaSite> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)
