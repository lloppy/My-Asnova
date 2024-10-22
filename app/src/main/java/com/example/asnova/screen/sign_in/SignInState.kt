package com.example.asnova.screen.sign_in

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    var signInError: String? = null
)