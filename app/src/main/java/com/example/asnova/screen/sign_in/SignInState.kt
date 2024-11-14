package com.example.asnova.screen.sign_in

import com.asnova.model.User

data class SignInState(
    val user: User? = null,
    val errorMessage: String? = null,
    val loading: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val otpSent: Boolean = false,
    val verificationId: String? = null
)