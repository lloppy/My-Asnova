package com.asnova.domain.repository.firebase

import com.asnova.model.Resource
import com.asnova.model.User

interface UserRepository {
    fun createUserWithEmailAndPassword(username: String, email: String, password: String, callback: (Resource<User?>) -> Unit)
    fun signInUserWithEmailAndPassword(email: String, password: String, callback: (Resource<User?>) -> Unit)
    fun signOutUser()
    fun isAuthedUser(callback: (Resource<Boolean>) -> Unit)
    fun pullRequest(callback: (Resource<User?>) -> Unit)
    fun addNewsItemToFavorites(id: String, callback: (Resource<Boolean>) -> Unit)
    fun getAllFavorites(callback: (Resource<List<String>>) -> Unit)
}