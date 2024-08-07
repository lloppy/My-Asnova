package com.example.asnova.screen.main.schedule

import com.asnova.model.AsnovaSchedule
import com.asnova.model.AsnovaSiteSchedule

data class AsnovaScheduleState(
    var value: List<AsnovaSchedule> = emptyList(),
    var valueFromSite: List<AsnovaSiteSchedule> = emptyList(),
    val error: String = "",
    val loading: Boolean = false
)
