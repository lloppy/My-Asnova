package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult

class RegisterWithEmailUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        role: String,
        fmc: String,
        callback: (Resource<SignInResult>) -> Unit
    ) {
        userRepository.registerWithEmail(email, password, role, fmc, callback)
    }
}