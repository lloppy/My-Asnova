package com.asnova.firebase.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val isAnonymous: Boolean = false,
    val photoUrl: String = "",
    val displayName: String = "",
    val email: String = "",
    val isEmailVerified: Boolean = false,
    val phoneNumber: String = "",
    val creationTimestamp: Timestamp = Timestamp.now(),
    val lastSignInTimestamp: Timestamp = Timestamp.now(),
    val favorites: List<String> = emptyList()
)
