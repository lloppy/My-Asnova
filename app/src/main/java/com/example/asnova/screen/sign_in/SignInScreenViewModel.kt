package com.example.asnova.screen.sign_in

import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asnova.domain.usecase.CreateUserWithPhoneUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.SignInUseCase
import com.asnova.domain.usecase.SignInWithIntentUseCase
import com.asnova.domain.usecase.SignInWithOtpUseCase
import com.asnova.model.Resource
import com.asnova.model.SignInResult
import com.example.asnova.data.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val userSharedPreferences: SharedPreferences,
    private val getUserDataUseCase: GetUserDataUseCase,

    private val signInUseCase: SignInUseCase,
    private val signInWithIntentUseCase: SignInWithIntentUseCase,

    private val signInWithOtpUseCase: SignInWithOtpUseCase,
    private val createUserWithPhoneUseCase: CreateUserWithPhoneUseCase

) : ViewModel() {
    // Справочник методов: https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/api-integration/api-description#Dostup-prilozheniya-k-dannym-polzovatelya
    // Настройка приложения: https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/application-settings#Nastrojka-dostupov

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        UserManager.init(userSharedPreferences)
        checkIfUserIsSignedIn()
    }

    fun signInWithOtp(otp: String, verificationId: String) {
        signInWithOtpUseCase(otp, verificationId) { resource ->
            val (user, errorMessage) = when (resource) {
                is Resource.Success -> {
                    val signInResult = resource.data
                    val user = signInResult?.data
                    Pair(user, null)
                }

                is Resource.Error -> {
                    Pair(null, resource.message)
                }

                else -> {
                    Pair(null, "Unknown error")
                }
            }
            onSignInResult(SignInResult(data = user, errorMessage = errorMessage))
        }
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

    suspend fun signInWithIntent(intent: Intent, role: String): SignInResult {
        return signInWithIntentUseCase.invoke(intent, role)
    }

    suspend fun signIn(): IntentSender? {
        return signInUseCase.invoke()
    }

    fun createUserWithPhone(mobile: String) {
        createUserWithPhoneUseCase(mobile) {

        }
    }
}