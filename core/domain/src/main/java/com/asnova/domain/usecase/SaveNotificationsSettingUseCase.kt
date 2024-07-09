package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.NotificationsSettingStorage
import com.asnova.model.NotificationsOption

class SaveNotificationsSettingUseCase(
    private val notificationsSettingStorage: NotificationsSettingStorage
) {
    operator fun invoke(notifications: NotificationsOption)
    {
        notificationsSettingStorage.save(notifications)
    }
}