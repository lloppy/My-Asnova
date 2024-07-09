package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.ThemeSettingRepository
import com.asnova.model.ThemeOption

class SaveThemeSettingUseCase(
    private val themeSettingRepository: ThemeSettingRepository
) {
    operator fun invoke(theme: ThemeOption)
    {
        themeSettingRepository.save(theme)
    }
}