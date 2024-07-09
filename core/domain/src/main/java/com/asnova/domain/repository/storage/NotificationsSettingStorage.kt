package com.asnova.domain.repository.storage

import com.asnova.model.NotificationsOption

interface NotificationsSettingStorage {
    fun save(notifications: NotificationsOption)
    fun get() : NotificationsOption
}