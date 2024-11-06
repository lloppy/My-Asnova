package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

class SelectClassUseCase (
    private val userRepository: UserRepository
) {
    operator fun invoke(asnovaClass: AsnovaStudentsClass?, callback: (Resource<Boolean>) -> Unit) {
        return userRepository.selectAsnovaClass(asnovaClass, callback)
    }
}