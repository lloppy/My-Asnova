package com.asnova.domain.usecase

import android.util.Log
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.User

class GetUserDataUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(callback: (Resource<User?>) -> Unit)
    {
        userRepository.getUserData(callback)
    }
}