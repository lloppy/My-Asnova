package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository

class CleanAsnovaClassesFromFirebaseUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Boolean) -> Unit) {
        return scheduleRepository.clearAsnovaClasses(callback)
    }
}