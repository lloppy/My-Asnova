package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult


class CreateUserWithPhoneUseCase (
    private val userRepository: UserRepository
) {
    operator fun invoke(phone: String, callback: (Resource<String>) -> Unit) {
        userRepository.createUserWithPhone(phone, callback)
    }
}