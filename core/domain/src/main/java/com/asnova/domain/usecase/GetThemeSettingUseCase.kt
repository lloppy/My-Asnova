package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.ThemeSettingRepository
import com.asnova.model.ThemeOption

class GetThemeSettingUseCase(
    private val themeSettingRepository: ThemeSettingRepository
) {
    operator fun invoke() : ThemeOption
    {
        return themeSettingRepository.get()
    }
}