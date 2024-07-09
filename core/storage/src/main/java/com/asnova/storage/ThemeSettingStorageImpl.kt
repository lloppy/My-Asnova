package com.asnova.storage

import android.content.Context
import com.asnova.model.ThemeOption
import com.asnova.domain.repository.storage.ThemeSettingStorage

const val SHARED_PREFS_THEME_SETTING = "shared_prefs_theme_setting"
const val KEY_THEME_SETTING = "theme_setting"

class ThemeSettingStorageImpl(context: Context) :
    ThemeSettingStorage {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_THEME_SETTING, Context.MODE_PRIVATE)
    override fun save(theme: ThemeOption) {
        sharedPreferences.edit().putString(KEY_THEME_SETTING, theme.value).apply()
    }

    override fun get(): ThemeOption {
        return ThemeOption(
            sharedPreferences.getString(
                KEY_THEME_SETTING,
                "DEFAULT"
            ) ?: "DEFAULT"
        )
    }
}