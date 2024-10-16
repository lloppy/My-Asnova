package com.asnova.storage

import android.content.Context
import com.asnova.domain.repository.storage.NotificationsSettingStorage
import com.asnova.model.NotificationsOption

const val SHARED_PREFS_NOTIFICATIONS_SETTING = "shared_prefs_notifications_setting"
const val KEY_NOTIFICATIONS_SETTING = "notifications_setting"

// Паттерн Bridge
class NotificationsSettingStorageImpl(context: Context) : NotificationsSettingStorage {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NOTIFICATIONS_SETTING, Context.MODE_PRIVATE)

    override fun save(notifications: NotificationsOption) {
        sharedPreferences.edit().putString(KEY_NOTIFICATIONS_SETTING, notifications.value).apply()
    }

    override fun get(): NotificationsOption {
        return NotificationsOption(
            sharedPreferences.getString(KEY_NOTIFICATIONS_SETTING, "ALL") ?: "ALL"
        )
    }
}