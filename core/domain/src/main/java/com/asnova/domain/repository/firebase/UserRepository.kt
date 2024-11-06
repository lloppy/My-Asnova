package com.asnova.domain.repository.firebase

import android.content.Intent
import android.content.IntentSender
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.asnova.model.SignInResult
import com.asnova.model.User

interface UserRepository {
    // авторизация
    fun signInWithIntent(
        intent: Intent,
        role: String,
        fmc: String,
        callback: (Resource<SignInResult>) -> Unit
    )

    fun signInWithOtp(
        otp: String,
        verificationId: String,
        callback: (Resource<SignInResult>) -> Unit
    )

    fun sendOtp(phone: String, callback: (Resource<String>) -> Unit)
    suspend fun signInWithLauncher(): IntentSender?
    fun signOut()


    // user data + check
    fun isAuthedUser(callback: (Resource<Boolean>) -> Unit)
    fun writeNewDataUser(
        name: String,
        surname: String,
        email: String,
        phone: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getUserData(callback: (Resource<User?>) -> Unit)
    fun checkUserData(callback: (Resource<Boolean>) -> Unit)
    fun checkUserClass(callback: (Resource<Boolean>) -> Unit)

    // role
    fun checkIsAdmin(callback: (Resource<Boolean>) -> Unit)
    fun submitPromocode(
        promocode: String,
        userData: User,
        callback: (Resource<String>) -> Unit
    )

    fun deleteAccount(callback: (Resource<Boolean>) -> Unit)
    fun selectAsnovaClass(asnovaClass: AsnovaStudentsClass, callback: (Resource<Boolean>) -> Unit)
}