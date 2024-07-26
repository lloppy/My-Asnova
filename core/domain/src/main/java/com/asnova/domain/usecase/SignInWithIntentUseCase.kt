package com.asnova.domain.usecase

import android.content.Intent
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.SignInResult

class SignInWithIntentUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(intent: Intent): SignInResult
    {
        return userRepository.signInWithIntent(intent)
    }
}