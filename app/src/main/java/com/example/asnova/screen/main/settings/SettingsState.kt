package com.example.asnova.screen.main.settings

import com.asnova.model.AsnovaStudentsClass

data class SettingsState(
    var asnovaClasses: List<AsnovaStudentsClass>? = null,
    val error: String = "",
    val loading: Boolean = false
)
