package com.asnova.domain.usecase

import android.content.IntentSender
import com.asnova.domain.repository.firebase.UserRepository

class SignInWithLauncher(
    private val userRepository: UserRepository
) {
    suspend fun invoke(): IntentSender? {
        return userRepository.signInWithLauncher()
    }
}