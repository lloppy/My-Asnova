package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource

class IsAuthedUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(callback: (Resource<Boolean>) -> Unit) {
        return userRepository.isAuthedUser(callback)
    }
}