package com.asnova.domain.repository.storage

import com.asnova.model.ThemeOption

interface ThemeSettingStorage {
    fun save(theme: ThemeOption)
    fun get() : ThemeOption
}