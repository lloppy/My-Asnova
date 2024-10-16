package com.asnova.domain.repository.storage

import com.asnova.model.ThemeOption

// Паттерн Bridge
interface ThemeSettingStorage : Storage<ThemeOption>{
    override fun save(theme: ThemeOption)
    override fun get() : ThemeOption
}