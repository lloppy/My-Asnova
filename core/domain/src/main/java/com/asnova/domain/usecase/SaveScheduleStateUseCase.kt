package com.asnova.domain.usecase

import com.asnova.model.LocalDate
import com.asnova.domain.repository.storage.ScheduleStateRepository

class SaveScheduleStateUseCase(
    private val scheduleStateRepository: ScheduleStateRepository
) {
    operator fun invoke(date: LocalDate)
    {
        scheduleStateRepository.save(date)
    }
}