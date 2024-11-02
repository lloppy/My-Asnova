package com.asnova.domain.usecase

import android.util.Log
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource

class SignOutUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(callback: (Resource<Unit>) -> Unit) {
        Log.d("SignOutUseCase", "Initiating sign out process.")

        userRepository.signOut { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("SignOutUseCase", "Sign out successful.")
                }
                is Resource.Error -> {
                    Log.e("SignOutUseCase", "Error during sign out: ${result.message}")
                }

                else -> {}
            }
            callback(result)
        }
    }
}