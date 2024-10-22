package com.example.asnova.screen.settings

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.GetAsnovaClassesUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.PushAsnovaClassesUseCase
import com.asnova.domain.usecase.SignOutUserUseCase
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.User
import com.asnova.storage.KEY_USER_SETTING
import com.example.asnova.data.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val signOutUserUseCase: SignOutUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val pushAsnovaClassesUseCase: PushAsnovaClassesUseCase,
    private val getAsnovaClassesUseCase: GetAsnovaClassesUseCase,
    private val userSharedPreferences: SharedPreferences
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    fun getUserData(callback: (Resource<User?>) -> Unit) {
        getUserDataUseCase.invoke(callback)
    }

    fun signOut() {
        signOutUserUseCase.invoke()

        // тут все равно переделывать придется , нужно на регистрацию кидать, а не на выход
        UserManager.signOut()
        userSharedPreferences.edit().putString(KEY_USER_SETTING, Role.NONE).apply()
    }

    fun getAsnovaClasses(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        Log.d("studentsClasses", "Fetching Asnova classes...")

        getAsnovaClassesUseCase.invoke(callback = { result ->
            Log.d("studentsClasses", "Received result from use case")
            handleAsnovaClassesResult(result)
        })
    }

    fun canLoadAdminAccess(): Boolean {
        return when (UserManager.getRole()) {
            Role.ADMIN -> true
            else -> false
        }
    }

    private fun handleAsnovaClassesResult(result: Resource<List<AsnovaStudentsClass>>) {
        Log.d("studentsClasses", "handleAsnovaClassesResult")

        when (result) {
            is Resource.Success -> {
                _state.value = SettingsState(asnovaClasses = result.data)
                Log.d("studentsClasses", "Success")
                Log.d("studentsClasses", _state.value.asnovaClasses?.first()?.name.toString())

            }

            is Resource.Error -> {
                _state.value =
                    SettingsState(error = result.message ?: "Ошибка. Проверьте интернет-соединение")
            }

            is Resource.Loading -> {
                _state.value = SettingsState(loading = true)
            }
        }
    }

    fun duplicateAsnovaClass(updatedClass: AsnovaStudentsClass) {
        _state.value.asnovaClasses = _state.value.asnovaClasses?.plus(updatedClass)
    }

    fun pushAsnovaClassesToFirebase(asnovaClasses: List<AsnovaStudentsClass>?, onSuccess: () -> Unit) {
        if (asnovaClasses.isNullOrEmpty()) {
            Log.w("pushAsnovaClasses", "No classes to push to Firebase.")
            return
        }

        pushAsnovaClassesUseCase.invoke(asnovaClasses) { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("pushAsnovaClasses", "Successfully pushed classes to Firebase.")
                    onSuccess()
                }

                is Resource.Error -> {
                    Log.e("pushAsnovaClasses", "Failed to push classes: ${result.message}")

                    _state.value =
                        SettingsState(error = result.message ?: "Ошибка сохранения учебных групп")
                }

                else -> {}
            }
        }
    }
}