package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.Resource
import com.asnova.model.Schedule

class GetScheduleUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Resource<List<Schedule>>) -> Unit) {
        return scheduleRepository.getAllSchedule(callback)
    }
}