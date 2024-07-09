package com.asnova.domain.usecase

import com.asnova.model.LocalDate
import com.asnova.domain.repository.storage.ScheduleStateRepository

class GetScheduleStateUseCase(
    private val scheduleStateRepository: ScheduleStateRepository
) {
    operator fun invoke() : LocalDate
    {
        return scheduleStateRepository.get()
    }
}