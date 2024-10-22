package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

class PushAsnovaClassesUseCase (
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(asnovaClasses: List<AsnovaStudentsClass>, callback: (Resource<Boolean>) -> Unit) {
        return scheduleRepository.pushAsnovaClasses(asnovaClasses, callback)
    }
}