package com.asnova.domain.repository.firebase

import com.asnova.model.Resource
import com.asnova.model.User

interface UserRepository {
    fun getAllFavorites(callback: (Resource<List<String>>) -> Unit)
    fun pullRequest(callback: (Resource<User?>) -> Unit)

    fun signOutUser()
    fun isAuthedUser(callback: (Resource<Boolean>) -> Unit)
}