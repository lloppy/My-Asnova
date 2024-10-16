package com.asnova.domain.repository.storage

import com.asnova.model.LanguageOption

// Паттерн Bridge
interface LanguageSettingStorage : Storage<LanguageOption> {
    override fun save(language: LanguageOption)
    override fun get(): LanguageOption
}