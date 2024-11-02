package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource


class CheckUserDataUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(callback: (Resource<Boolean>) -> Unit) {
        return userRepository.checkUserData(callback)
    }
}