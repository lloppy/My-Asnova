package com.asnova.domain.usecase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult

class SignInWithOtpUseCase (
    private val userRepository: UserRepository
) {
    operator fun invoke(otp: String, verificationId: String, callback: (Resource<SignInResult>) -> Unit) {
        userRepository.signInWithOtp(otp, verificationId, callback)
    }
}