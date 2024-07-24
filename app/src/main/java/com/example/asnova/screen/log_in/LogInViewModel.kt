package com.example.asnova.screen.log_in

import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asnova.storage.KEY_USER_SETTING
import com.example.asnova.data.UserManager
import com.example.asnova.screen.log_in.services.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val userSharedPreferences: SharedPreferences

) : ViewModel() {
    // Справочник методов: https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/api-integration/api-description#Dostup-prilozheniya-k-dannym-polzovatelya
    // Настройка приложения: https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/application-settings#Nastrojka-dostupov

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        checkIfUserIsSignedIn()
        getUserStatusFromSharedPref()
    }

    private fun getUserStatusFromSharedPref() {
        val userStatus = userSharedPreferences.getString(KEY_USER_SETTING, false.toString())
        UserManager.status = when (userStatus) {
            true.toString() -> true
            false.toString() -> false
            else -> false
        }
        Log.e("login_info", "UserManager status is $userStatus")
    }

    private fun checkIfUserIsSignedIn() {
        viewModelScope.launch {
            googleAuthUiClient.getSignedInUser()?.let { user ->
                onSignInResult(SignInResult(data = user, errorMessage = null))
            }
        }
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        return googleAuthUiClient.signInWithIntent(intent)
    }

    suspend fun signIn(): IntentSender? {
        return googleAuthUiClient.signIn()
    }
}