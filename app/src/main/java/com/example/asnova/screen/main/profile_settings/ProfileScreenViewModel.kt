package com.example.asnova.screen.main.profile_settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.SignOutUserUseCase
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.User
import com.asnova.storage.KEY_USER_SETTING
import com.example.asnova.data.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val signOutUserUseCase: SignOutUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,

    private val userSharedPreferences: SharedPreferences
) : ViewModel() {

    fun getUserData(callback: (Resource<User?>) -> Unit) {
        getUserDataUseCase.invoke(callback)
    }

    fun signOut() {
        signOutUserUseCase.invoke()

        // тут все равно переделывать придется , нужно на регистрацию кидать, а не на выход
        UserManager.signOut()
        userSharedPreferences.edit().putString(KEY_USER_SETTING, Role.NONE).apply()
    }
}