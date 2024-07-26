package com.asnova.domain.usecase

import android.content.IntentSender
import com.asnova.domain.repository.firebase.UserRepository

class SignInUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): IntentSender?
    {
        return userRepository.signIn()
    }
}