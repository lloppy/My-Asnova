package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.AsnovaClassRepository
import com.asnova.model.Resource

class DeleteAsnovaClassUseCase (
    private val asnovaClassRepository: AsnovaClassRepository
) {
    operator fun invoke(classId: Int, callback: (Resource<Unit>) -> Unit) {
        return asnovaClassRepository.deleteAsnovaClass(classId, callback)
    }
}