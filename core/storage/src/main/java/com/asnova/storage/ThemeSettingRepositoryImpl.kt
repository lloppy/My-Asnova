package com.asnova.storage

import com.asnova.domain.repository.storage.ThemeSettingRepository
import com.asnova.model.ThemeOption
import com.asnova.domain.repository.storage.ThemeSettingStorage

class ThemeSettingRepositoryImpl(
    private val themeSettingStorage: ThemeSettingStorage
) : ThemeSettingRepository {
    override fun save(theme: ThemeOption) {
        themeSettingStorage.save(theme)
    }

    override fun get(): ThemeOption {
        return themeSettingStorage.get()
    }
}