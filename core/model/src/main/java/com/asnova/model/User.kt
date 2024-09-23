package com.asnova.model

data class User(
    val userId: String = "",
    val username: String? = "",
    val email: String? = "",
    val profilePictureUrl: String? = "",
    val role: String? = null
)

object Role {
    const val ADMIN = "Администратор"
    const val WORKER = "Сотрудник"
    const val STUDENT = "Учащийся"
    const val VISITOR = "Гость"
    const val NONE = "Вход не выполнен"
}

