package com.asnova.domain.usecase

import com.asnova.domain.repository.storage.NotificationsSettingStorage
import com.asnova.model.NotificationsOption

class GetNotificationsSettingUseCase(
    private val notificationsSettingStorage: NotificationsSettingStorage
) {
    operator fun invoke() : NotificationsOption
    {
        return notificationsSettingStorage.get()
    }
}