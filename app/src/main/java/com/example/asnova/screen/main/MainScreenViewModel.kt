package com.example.asnova.screen.main

import androidx.lifecycle.ViewModel
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.User
import com.example.asnova.data.UserManager
import com.example.asnova.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _bottomItems = listOf(Screen.Feed, Screen.Schedule, Screen.ProfileSettings)
    val bottomItems = _bottomItems
    var userData: User? = null
    private val role = UserManager.getRole()

    init {
        userRepository.getUserData { resource ->
            userData = resource.data
        }
    }

    fun checkUserData(callback: (Boolean) -> Unit) {
        userRepository.checkUserData { resource ->
            resource.data?.let { callback(it) }
        }
    }

    fun writeNewDataUser(name: String, surname: String, email: String, phone: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userRepository.writeNewDataUser(name, surname, email, phone, onSuccess, onFailure)
    }

}