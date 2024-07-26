package com.example.asnova.screen.log_in

import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.SignInUseCase
import com.asnova.domain.usecase.SignInWithIntentUseCase
import com.asnova.model.SignInResult
import com.asnova.storage.KEY_USER_SETTING
import com.example.asnova.data.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val userSharedPreferences: SharedPreferences,
    private val getUserDataUseCase: GetUserDataUseCase,

    private val signInUseCase: SignInUseCase,
    private val signInWithIntentUseCase: SignInWithIntentUseCase

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
            getUserDataUseCase.invoke { resource ->
                val user = resource.data
                if (user != null) {
                    onSignInResult(SignInResult(data = user, errorMessage = null))
                } else {
                    onSignInResult(SignInResult(data = null, errorMessage = "User not signed in"))
                }
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
        return signInWithIntentUseCase.invoke(intent)
    }

    suspend fun signIn(): IntentSender? {
        return signInUseCase.invoke()
    }
}