package com.example.asnova.screen.main.profile_settings

import androidx.lifecycle.ViewModel
import com.example.asnova.screen.log_in.services.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient

): ViewModel() {
    fun getGoogleAuthUiClient(): GoogleAuthUiClient {
        return googleAuthUiClient
    }

    suspend fun signOut() {
        googleAuthUiClient.signOut()
    }
}