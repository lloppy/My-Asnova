package com.example.asnova.screen.log_in

import com.example.asnova.data.UserData

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)