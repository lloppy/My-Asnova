package com.asnova.model

data class User(
    val userUid: String = "",
    val username: String? = "",

    val name: String? = "",
    val surname: String? = "",
    val email: String? = "",
    val phone: String = "",

    val profilePictureUrl: String? = "",
    val role: String? = null
)
