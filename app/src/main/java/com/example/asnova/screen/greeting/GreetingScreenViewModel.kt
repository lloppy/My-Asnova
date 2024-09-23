package com.example.asnova.screen.greeting

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asnova.model.Role
import com.asnova.storage.KEY_USER_SETTING
import com.example.asnova.data.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GreetingScreenViewModel @Inject constructor(
    private val userSharedPreferences: SharedPreferences

) : ViewModel() {

    private val _state = mutableStateOf(GreetingState())
    val state: State<GreetingState> get() = _state

    fun onRoleSelected(role: String) {
        _state.value = _state.value.copy(selectedRole = role)
        UserManager.setRole(role)
        onSaveUserManagerRole(role)
    }


    private fun onSaveUserManagerRole(newRole: String) {
        userSharedPreferences.edit().putString(KEY_USER_SETTING, newRole).apply()
        Log.e(
            "login_info",
            "Save role userSnow haredPreferences. UserManager role is ${UserManager.getRole()}"
        )
    }
}
