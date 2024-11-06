package com.asnova.firebase

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.asnova.model.SignInResult
import com.asnova.model.User

// Паттерн Proxy
class LoggingUserRepository(private val repository: UserRepository) : UserRepository {
    private val tag = "LoggingUserRepository"

    private fun logResourceResult(methodName: String, resource: Resource<*>) {
        when (resource) {
            is Resource.Loading -> Log.d(tag, "$methodName called - Loading")
            is Resource.Success -> Log.d(tag, "$methodName result: ${resource.data}, message: ${resource.message}")
            is Resource.Error -> Log.e(tag, "$methodName error: ${resource.message}")
        }
    }

    override fun writeNewDataUser(
        name: String,
        surname: String,
        email: String,
        phone: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Log.d(tag, "writeNewDataUser called with name: $name, surname: $surname")
        repository.writeNewDataUser(name, surname, email, phone, {
            Log.d(tag, "writeNewDataUser success")
            onSuccess()
        }, { error ->
            Log.e(tag, "writeNewDataUser failed: $error")
            onFailure(error)
        })
    }

    override fun signOut() {
        Log.d(tag, "signOut called")
        repository.signOut()
    }

    override fun isAuthedUser(callback: (Resource<Boolean>) -> Unit) {
        Log.d(tag, "isAuthedUser called")
        repository.isAuthedUser { resource ->
            logResourceResult("isAuthedUser", resource)
            callback(resource)
        }
    }

    override fun checkUserData(callback: (Resource<Boolean>) -> Unit) {
        Log.d(tag, "checkUserData called")
        repository.checkUserData { resource ->
            logResourceResult("checkUserData", resource)
            callback(resource)
        }
    }

    override fun checkUserClass(callback: (Resource<Boolean>) -> Unit) {
        Log.d(tag, "checkUserClass called")
        repository.checkUserClass { resource ->
            logResourceResult("checkUserClass", resource)
            callback(resource)
        }
    }

    override fun getUserData(callback: (Resource<User?>) -> Unit) {
        Log.d(tag, "getUserData called")
        repository.getUserData { resource ->
            logResourceResult("getUserData", resource)
            callback(resource)
        }
    }

    override suspend fun signInWithLauncher(): IntentSender? {
        Log.d(tag, "signInWithLauncher called")
        return repository.signInWithLauncher()
    }

    override fun signInWithIntent(
        intent: Intent,
        role: String,
        fmc: String,
        callback: (Resource<SignInResult>) -> Unit
    ) {
        Log.d(tag, "signInWithIntent called")
        repository.signInWithIntent(intent, role, fmc) { resource ->
            logResourceResult("signInWithIntent", resource)
            callback(resource)
        }
    }

    override fun checkIsAdmin(callback: (Resource<Boolean>) -> Unit) {
        Log.d(tag, "checkIsAdmin called")
        repository.checkIsAdmin { resource ->
            logResourceResult("checkIsAdmin", resource)
            callback(resource)
        }
    }

    override fun submitPromocode(
        promocode: String,
        userData: User,
        callback: (Resource<String>) -> Unit
    ) {
        Log.d(tag, "submitPromocode called with promocode: $promocode")
        repository.submitPromocode(promocode, userData) { resource ->
            logResourceResult("submitPromocode", resource)
            callback(resource)
        }
    }

    override fun deleteAccount(callback: (Resource<Boolean>) -> Unit) {
        Log.d(tag, "deleteAccount called")
        repository.deleteAccount { resource ->
            logResourceResult("deleteAccount", resource)
            callback(resource)
        }
    }

    override fun selectAsnovaClass(
        asnovaClass: AsnovaStudentsClass?,
        callback: (Resource<Boolean>) -> Unit
    ) {
        Log.d(tag, "selectAsnovaClass called with class: ${asnovaClass?.name}")
        repository.selectAsnovaClass(asnovaClass) { resource ->
            logResourceResult("selectAsnovaClass", resource)
            callback(resource)
        }
    }

    override fun signInWithOtp(
        otp: String,
        verificationId: String,
        callback: (Resource<SignInResult>) -> Unit
    ) {
        Log.d(tag, "signInWithOtp called")
        repository.signInWithOtp(otp, verificationId) { resource ->
            logResourceResult("signInWithOtp", resource)
            callback(resource)
        }
    }

    override fun sendOtp(phone: String, callback: (Resource<String>) -> Unit) {
        Log.d(tag, "sendOtp called for phone number: $phone")
        repository.sendOtp(phone) { resource ->
            logResourceResult("sendOtp", resource)
            callback(resource)
        }
    }
}