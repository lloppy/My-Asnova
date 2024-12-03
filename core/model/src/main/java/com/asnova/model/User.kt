package com.asnova.model

data class User(
    val userUid: String = "",
    val username: String? = "",
    val passwordHash: String = "",

    val name: String? = "",
    val surname: String? = "",
    val email: String? = "",
    val phone: String = "",
    val fmc: String = "",
    val asnovaClass: String = "",

    val profilePictureUrl: String? = "",
    val role: String? = null
) {
    override fun toString(): String {
        return "User is (userUid='$userUid', username=$username, passwordHash='$passwordHash', name=$name, surname=$surname, email=$email, phone='$phone', fmc='$fmc', asnovaClass='$asnovaClass', profilePictureUrl=$profilePictureUrl, role=$role)"
    }
}
