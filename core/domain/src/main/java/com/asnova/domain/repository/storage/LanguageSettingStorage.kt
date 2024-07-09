package com.asnova.domain.repository.storage

import com.asnova.model.LanguageOption


interface LanguageSettingStorage {
    fun save(language: LanguageOption)
    fun get() : LanguageOption
}