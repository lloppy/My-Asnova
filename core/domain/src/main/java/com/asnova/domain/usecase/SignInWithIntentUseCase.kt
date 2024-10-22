package com.asnova.domain.usecase

import android.content.Intent
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.SignInResult
import com.asnova.model.WallItem

class SignInWithIntentUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(intent: Intent, role: String, fmc: String, callback: (Resource<SignInResult>) -> Unit) {
        userRepository.signInWithIntent(intent, role, fmc, callback)
    }
}