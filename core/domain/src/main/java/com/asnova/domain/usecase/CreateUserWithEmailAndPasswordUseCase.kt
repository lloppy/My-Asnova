package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.User

class CreateUserWithEmailAndPasswordUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(username: String, email: String, password: String, callback: (Resource<User?>) -> Unit) {
        return userRepository.createUserWithEmailAndPassword(username, email, password, callback)
    }
}