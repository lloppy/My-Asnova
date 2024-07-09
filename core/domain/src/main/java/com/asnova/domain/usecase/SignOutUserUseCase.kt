package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository

class SignOutUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke() {
        return userRepository.signOutUser()
    }
}