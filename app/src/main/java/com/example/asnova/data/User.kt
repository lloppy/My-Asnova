package com.example.asnova.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

val defaultImageUrl =
    "https://sun9-78.userapi.com/impg/Ir5UOUAUw9qczne8EVGjGw_wWvEK_Dsv_awN9Q/qguEM4hhSLA.jpg?size=1953x989&quality=96&sign=86ca45843194e357c1ea8ba559dc6117&type=album"

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String? = defaultImageUrl
)

object UserManager {
    var status by mutableStateOf(false)
}