package com.example.asnova.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)

object UserManager {
    var status by mutableStateOf(false)
}