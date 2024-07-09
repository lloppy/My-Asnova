package com.asnova.model

data class User(
    val uid: String,
    val isAnonymous: Boolean,
    val photoUrl: String,
    val displayName: String,
    val email: String,
    val isEmailVerified: Boolean,
    val phoneNumber: String,
    val creationTimestamp: Long?,
    val lastSignInTimestamp: Long?,
    val favorites: List<String>
)
