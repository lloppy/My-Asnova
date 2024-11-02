package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.User

class SubmitPromocodeUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        promocode: String,
        userData: User,
        callback: (Resource<String>) -> Unit
    ) {
        userRepository.submitPromocode(promocode,userData, callback)
    }
}