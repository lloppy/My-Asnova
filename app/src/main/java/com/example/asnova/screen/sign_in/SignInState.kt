package com.example.asnova.screen.sign_in

data class SignInState(
    val errorMessage: String? = null,
    val loading: Boolean = false,
    val isSignInSuccessful: Boolean = false,
    val otpSent: Boolean = false,
    val verificationId: String? = null
)