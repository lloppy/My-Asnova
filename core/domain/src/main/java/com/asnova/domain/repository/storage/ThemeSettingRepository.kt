package com.asnova.domain.repository.storage

import com.asnova.model.ThemeOption

interface ThemeSettingRepository {
    fun save(theme: ThemeOption)
    fun get() : ThemeOption
}