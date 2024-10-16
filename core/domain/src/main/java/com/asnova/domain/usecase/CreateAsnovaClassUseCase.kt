package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.AsnovaClassRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

class CreateAsnovaClassUseCase (
    private val asnovaClassRepository: AsnovaClassRepository
) {
    operator fun invoke(newClass: AsnovaStudentsClass, callback: (Resource<AsnovaStudentsClass>) -> Unit) {
        return asnovaClassRepository.createAsnovaClass(newClass, callback)
    }
}