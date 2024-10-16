package com.asnova.domain.repository.storage

import com.asnova.model.NotificationsOption

// Паттерн Bridge
interface NotificationsSettingStorage : Storage<NotificationsOption>{
    override fun save(notifications: NotificationsOption)
    override fun get() : NotificationsOption
}