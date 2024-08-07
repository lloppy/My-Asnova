package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaSchedule
import com.asnova.model.Resource

class GetScheduleUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Resource<List<AsnovaSchedule>>) -> Unit) {
        // получаем расписание только из календаря
        return scheduleRepository.getScheduleFromCalDav(callback)
    }
}