package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.LanguageSettingStorage
import com.asnova.model.LanguageOption

class GetLanguageSettingUseCase(
    private val languageSettingStorage: LanguageSettingStorage
) {
    operator fun invoke() : LanguageOption
    {
        return languageSettingStorage.get()
    }
}