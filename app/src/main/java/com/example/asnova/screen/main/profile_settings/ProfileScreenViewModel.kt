package com.example.asnova.screen.main.profile_settings

import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.GetSafetyNewsUseCase
import com.asnova.domain.usecase.SignOutUserUseCase
import com.example.asnova.screen.log_in.services.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val signOutUserUseCase: SignOutUserUseCase,

    private val googleAuthUiClient: GoogleAuthUiClient

): ViewModel() {
    fun getGoogleAuthUiClient(): GoogleAuthUiClient {
        return googleAuthUiClient
    }

    suspend fun signOut() {
        signOutUserUseCase.invoke()
        //googleAuthUiClient.signOut()
    }
}