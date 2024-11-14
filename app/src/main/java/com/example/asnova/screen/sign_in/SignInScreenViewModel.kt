package com.example.asnova.screen.sign_in

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asnova.domain.usecase.*
import com.asnova.model.Resource
import com.asnova.model.SignInResult
import com.asnova.model.User
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
    private val signInWithLauncher: SignInWithLauncher,
    private val signInWithIntentUseCase: SignInWithIntentUseCase,
    private val signInWithOtpUseCase: SignInWithOtpUseCase,
    private val signInWithPhoneUseCase: SignInWithPhoneUseCase
   // private val createUserWithPhoneUseCase: CreateUserWithPhoneUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        UserManager.init(userSharedPreferences)
        checkIfUserIsSignedIn()
    }

    fun createUserWithPhone(mobile: String, activity: Activity, onFault: (String) -> Unit) {
        signInWithPhoneUseCase(mobile, activity) { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data != null) {
                        _state.update {
                            it.copy(otpSent = true, verificationId = resource.data!!.errorMessage)
                        }
                    } else {
                        _state.update {
                            it.copy(otpSent = true, verificationId = resource.data?.errorMessage)
                        }
                        onFault(resource.message ?: "Unknown error")
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(errorMessage = resource.message) }
                    onFault(resource.message ?: "Unknown error")
                }

                else -> {
                    onFault("Unknown error")
                }
            }
        }
    }

    fun signInWithOtp(otp: String, onFault: (String) -> Unit) {
        val verificationId = _state.value.verificationId ?: return
        _state.update { it.copy(loading = true) }

        signInWithOtpUseCase(otp, verificationId) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val user = resource.data?.data
                    _state.update {
                        it.copy(user = user, errorMessage = null, isSignInSuccessful = user != null, loading = false)
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(user = null, errorMessage = resource.message, isSignInSuccessful = false, loading = false)
                    }
                    onFault(resource.message ?: "Unknown error")
                }
                else -> {
                    _state.update {
                        it.copy(user = null, errorMessage = "Unknown error", isSignInSuccessful = false, loading = false)
                    }
                    onFault("Unknown error")
                }
            }
        }
    }

    private fun checkIfUserIsSignedIn() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            getUserDataUseCase.invoke { resource ->
                val user = resource.data
                if (user != null) {
                    _state.update { it.copy(user = user, errorMessage = null, isSignInSuccessful = true, loading = false) }
                } else {
                    _state.update { it.copy(user = null, errorMessage = "User not signed in", isSignInSuccessful = false, loading = false) }
                }
            }
        }
    }

    fun resetState() {
        _state.value = SignInState()
    }

    fun signInWithIntent(intent: Intent, role: String, fmc: String) {
        _state.update { it.copy(loading = true) }

        signInWithIntentUseCase.invoke(intent, role, fmc) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val user = resource.data?.data
                    _state.update {
                        it.copy(user = user, errorMessage = null, isSignInSuccessful = user != null, loading = false)
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(user = null, errorMessage = resource.message, isSignInSuccessful = false, loading = false)
                    }
                }
                else -> {
                    _state.update {
                        it.copy(user = null, errorMessage = "Unknown error", isSignInSuccessful = false, loading = false)
                    }
                }
            }
        }
    }

    suspend fun signInWithLauncher(): IntentSender? {
        return signInWithLauncher.invoke()
    }
}