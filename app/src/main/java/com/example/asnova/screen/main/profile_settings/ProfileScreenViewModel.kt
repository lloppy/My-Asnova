package com.example.asnova.screen.main.profile_settings

import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.GetSafetyNewsUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.SignOutUserUseCase
import com.asnova.model.Resource
import com.asnova.model.User
import com.example.asnova.screen.log_in.services.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val signOutUserUseCase: SignOutUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,

    private val googleAuthUiClient: GoogleAuthUiClient

): ViewModel() {
    fun getGoogleAuthUiClient(): GoogleAuthUiClient {
        return googleAuthUiClient
    }

    fun getUserData(callback: (Resource<User?>) -> Unit) {
        getUserDataUseCase.invoke(callback)
    }

    fun signOut() {
        signOutUserUseCase.invoke()
    }
}