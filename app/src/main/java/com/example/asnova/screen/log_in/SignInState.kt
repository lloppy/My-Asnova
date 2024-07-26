package com.example.asnova.screen.log_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)