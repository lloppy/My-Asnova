package com.asnova.domain.usecase

import android.app.Activity
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult

class SignInWithPhoneUseCase (
    private val userRepository: UserRepository
) {
    operator fun invoke(phone: String, activity: Activity, callback: (Resource<SignInResult>) -> Unit) {
        userRepository.signInWithPhone(phone, activity, callback)
    }
}