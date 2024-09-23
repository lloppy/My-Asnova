package com.example.asnova.screen.greeting

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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

    fun onRoleSelected(role: String, onSuccess: () -> Unit) {
        _state.value = _state.value.copy(selectedRole = role)
        UserManager.setRole(role)

        if (onSaveUserManagerRole(role)) {
            onSuccess()
        }
    }

    private fun onSaveUserManagerRole(newRole: String): Boolean {
        return try {
            userSharedPreferences.edit().putString(KEY_USER_SETTING, newRole).apply()
            Log.e(
                "login_info",
                "Save role user in SharedPreferences. UserManager role is ${UserManager.getRole()}"
            )
            true
        } catch (e: Exception) {
            Log.e("login_info", "Error saving role: ${e.message}")
            false
        }
    }
}
