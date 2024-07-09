package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.LanguageSettingStorage
import com.asnova.model.LanguageOption

class SaveLanguageSettingUseCase(
    private val languageSettingStorage: LanguageSettingStorage
) {
    operator fun invoke(language: LanguageOption)
    {
        languageSettingStorage.save(language)
    }
}