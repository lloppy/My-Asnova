package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.User

class SignInUserWithEmailAndPasswordUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(email: String, password: String, callback: (Resource<User?>) -> Unit) {
        return userRepository.signInUserWithEmailAndPassword(email, password, callback)
    }
}