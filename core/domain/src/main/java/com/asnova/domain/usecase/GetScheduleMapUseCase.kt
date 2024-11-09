package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.Resource
import com.asnova.model.ScheduleAsnovaPrivate
import java.time.LocalDate

class GetScheduleMapUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Resource<Map<LocalDate, List<ScheduleAsnovaPrivate>>>) -> Unit) {
        return scheduleRepository.getPrivateMapSchedule(callback)
    }
}