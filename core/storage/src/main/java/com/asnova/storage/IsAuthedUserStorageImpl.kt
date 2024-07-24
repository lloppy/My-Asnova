package com.asnova.storage

import android.content.Context
import com.asnova.domain.repository.storage.IsAuthedUserStorage

const val SHARED_PREFS_USER_SETTING = "shared_prefs_user_setting"
const val KEY_USER_SETTING = "user_setting"

class IsAuthedUserStorageImpl(context: Context) : IsAuthedUserStorage {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_USER_SETTING, Context.MODE_PRIVATE)
    override fun save(status: String) {
        sharedPreferences.edit().putString(KEY_USER_SETTING, status).apply()
    }

    override fun get(): String {
        return sharedPreferences.getString(KEY_USER_SETTING, false.toString()) ?: false.toString()
    }
}