package com.asnova.firebase.model

data class User(
    val userId: String = "",
    val username: String? = "",
    val email: String? = "",
    val profilePictureUrl: String? = ""
)
