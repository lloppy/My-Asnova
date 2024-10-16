package com.asnova.domain.repository.firebase

import android.content.Intent
import android.content.IntentSender
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.SignInResult
import com.asnova.model.User

interface UserRepository {
    fun getAllFavorites(callback: (Resource<List<String>>) -> Unit)
    fun pullRequest(callback: (Resource<User?>) -> Unit)

    fun isAuthedUser(callback: (Resource<Boolean>) -> Unit)
    fun getUserData(callback: (Resource<User?>) -> Unit)
    fun checkUserData(callback: (Resource<Boolean>) -> Unit)
    fun checkIsAdmin(callback: (Resource<Boolean>) -> Unit)
    fun writeNewDataUser(name: String, surname: String, email: String, phone: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent, role: String): SignInResult
    fun signInWithOtp(otp: String, verificationId: String, callback: (Resource<SignInResult>) -> Unit)
    fun createUserWithPhone(phone: String, callback: (Resource<String>) -> Unit)
    fun signOut()
}