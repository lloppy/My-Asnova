package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult

class OneTapSignInUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(callback: (Resource<SignInResult>) -> Unit)
    {
        userRepository.oneTapSignIn(callback)
    }
}