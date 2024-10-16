package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.AsnovaClassRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource

class UpdateAsnovaClassUseCase (
    private val asnovaClassRepository: AsnovaClassRepository
) {
    operator fun invoke(updatedClass: AsnovaStudentsClass, callback: (Resource<AsnovaStudentsClass>) -> Unit) {
        return asnovaClassRepository.updateAsnovaClass(updatedClass, callback)
    }
}