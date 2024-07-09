package com.asnova.storage

import android.content.Context
import com.asnova.domain.repository.storage.LanguageSettingStorage
import com.asnova.model.LanguageOption

const val SHARED_PREFS_LANGUAGE_SETTING = "shared_prefs_language_setting"
const val KEY_LANGUAGE_SETTING = "language_setting"

class LanguageSettingStorageImpl(context: Context) : LanguageSettingStorage {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_LANGUAGE_SETTING, Context.MODE_PRIVATE)
    override fun save(language: LanguageOption) {
        sharedPreferences.edit().putString(KEY_LANGUAGE_SETTING, language.value).apply()
    }

    override fun get(): LanguageOption {
        return LanguageOption(
            sharedPreferences.getString(
                KEY_LANGUAGE_SETTING,
                "DEFAULT"
            ) ?: "DEFAULT"
        )
    }
}