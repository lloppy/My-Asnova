package com.example.asnova.screen.main.schedule

import com.asnova.model.AsnovaSchedule
import com.asnova.model.AsnovaSiteSchedule

data class AsnovaScheduleState(
    var privateSchedule: List<AsnovaSchedule> = emptyList(),
    var siteSchedule: List<AsnovaSiteSchedule> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)
