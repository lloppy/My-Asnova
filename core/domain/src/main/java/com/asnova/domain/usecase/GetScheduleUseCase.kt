package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.ScheduleAsnovaPrivate
import com.asnova.model.Resource

class GetScheduleUseCase(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Resource<List<ScheduleAsnovaPrivate>>) -> Unit) {
        // получаем расписание только из календаря
        return scheduleRepository.getScheduleFromCalDav(callback)
    }
}