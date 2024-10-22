package com.asnova.firebase.export

// Паттерн Composite
data class UserNode(
    val userUid: String,
    val username: String,
    val email: String
) : DatabaseNode {
    override fun getData(): Any {
        return mapOf("userUid" to userUid, "username" to username, "email" to email)
    }
}

data class AdminNode(
    val adminUid: String,
    val email: String
) : DatabaseNode {
    override fun getData(): Any {
        return mapOf("adminUid" to adminUid, "email" to email)
    }
}

data class ClassNode(
    val classId: String,
    val name: String
) : DatabaseNode {
    override fun getData(): Any {
        return mapOf("classId" to classId, "name" to name)
    }
}