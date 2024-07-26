package com.asnova.domain.repository.firebase

import android.content.Intent
import android.content.IntentSender
import com.asnova.model.Resource
import com.asnova.model.SignInResult
import com.asnova.model.User

interface UserRepository {
    fun getAllFavorites(callback: (Resource<List<String>>) -> Unit)
    fun pullRequest(callback: (Resource<User?>) -> Unit)

    fun isAuthedUser(callback: (Resource<Boolean>) -> Unit)
    fun getUserData(callback: (Resource<User?>) -> Unit)

    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): SignInResult
    fun signOut()
}