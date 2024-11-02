package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

class GetAsnovaClassesFromFirebaseUseCase (
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        return scheduleRepository.getAsnovaClassesFromFirebase(callback)
    }
}