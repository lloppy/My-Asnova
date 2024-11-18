package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult

class SignInWithEmailUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        callback: (Resource<SignInResult>) -> Unit
    ) {
        userRepository.signInWithEmail(email, password, callback)
    }
}