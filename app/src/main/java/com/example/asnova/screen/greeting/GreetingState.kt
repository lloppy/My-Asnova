package com.example.asnova.screen.greeting

data class GreetingState(
    val selectedRole: String? = null,
    val error: String = "",
    val loading: Boolean = false
)