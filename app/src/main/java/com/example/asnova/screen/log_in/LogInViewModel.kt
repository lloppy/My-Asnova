package com.example.asnova.screen.log_in

import androidx.lifecycle.ViewModel
import com.example.asnova.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(

): ViewModel() {
    // Справочник методов: https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/api-integration/api-description#Dostup-prilozheniya-k-dannym-polzovatelya
    // Настройка приложения: https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/application-settings#Nastrojka-dostupov

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

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
}

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)