package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource


class CreateUserWithPhoneUseCase (
    private val userRepository: UserRepository
) {
    operator fun invoke(phone: String, callback: (Resource<String>) -> Unit) {
        userRepository.sendOtp(phone, callback)
    }
}