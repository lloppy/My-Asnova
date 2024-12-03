package com.example.asnova.screen.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.domain.repository.storage.IsAuthedUserStorage
import com.asnova.domain.usecase.CheckUserDataUseCase
import com.asnova.model.Role
import com.asnova.model.User
import com.example.asnova.data.UserManager
import com.example.asnova.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val checkUserDataUseCase: CheckUserDataUseCase,

    private val isAuthedUserStorage: IsAuthedUserStorage
) : ViewModel() {
    private val _bottomItems = listOf(Screen.Feed, Screen.Schedule, Screen.ProfileSettings)
    val bottomItems = _bottomItems
    var userData: User? = null
    private var role = UserManager.getRole()

    init {
        userRepository.getUserData { resource ->
            userData = resource.data
        }

        userRepository.checkIsAdmin{ isAdmin ->
            if (isAdmin.data == true) {
                UserManager.setRole(Role.ADMIN)
                role = Role.ADMIN
                Log.d("UserManager", "${UserManager.getRole()}")
                isAuthedUserStorage.save(Role.ADMIN)
            }
        }
    }

    fun checkUserData(callback: (Boolean) -> Unit) {
        checkUserDataUseCase { resource ->
            resource.data?.let { callback(it) }
        }
    }

    fun writeNewDataUser(name: String, surname: String, email: String, phone: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userRepository.updateUserInfo(name, surname, email, phone, onSuccess, onFailure)
    }

    fun saveDontAskAgainPreference(context: Context, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("dont_ask_again", value)
            apply()
        }
    }

    fun shouldShowDialog(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return !sharedPreferences.getBoolean("dont_ask_again", false)
    }


}