package com.example.asnova.screen.settings

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.domain.repository.storage.IsAuthedUserStorage
import com.asnova.domain.usecase.CheckIsAdminUseCase
import com.asnova.domain.usecase.CheckUserDataUseCase
import com.asnova.domain.usecase.GetAsnovaClassesUseCase
import com.asnova.domain.usecase.GetUserDataUseCase
import com.asnova.domain.usecase.PushAsnovaClassesUseCase
import com.asnova.domain.usecase.SignOutUserUseCase
import com.asnova.domain.usecase.SubmitPromocodeUseCase
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
    private val userRepository: UserRepository,

    private val signOutUserUseCase: SignOutUserUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,

    private val pushAsnovaClassesUseCase: PushAsnovaClassesUseCase,
    private val getAsnovaClassesUseCase: GetAsnovaClassesUseCase,

    private val submitPromocodeUseCase: SubmitPromocodeUseCase,

    private val isAuthedUserStorage: IsAuthedUserStorage,
    private val checkIsAdminUseCase: CheckIsAdminUseCase,
    private val userSharedPreferences: SharedPreferences
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state
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

    fun writeNewDataUser(name: String, surname: String, email: String, phone: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userRepository.writeNewDataUser(name, surname, email, phone, onSuccess, onFailure)
    }

    fun getUserData(callback: (Resource<User?>) -> Unit) {
        getUserDataUseCase.invoke(callback)
    }

    fun signOut() {
        signOutUserUseCase.invoke()

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

    fun pushAsnovaClassesToFirebase(
        asnovaClasses: List<AsnovaStudentsClass>?,
        onSuccess: () -> Unit
    ) {
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

    fun submitPromocode(promocode: String, context: Context, navController: NavController) {
        getUserData { resource ->
            when (resource) {
                is Resource.Success -> {
                    val userData = resource.data
                    if (userData != null) {
                        submitPromocodeUseCase(
                            promocode = promocode,
                            userData = userData,
                            callback = { result ->
                                Toast.makeText(
                                    context,
                                    "Промокод успешно отправлен на проверку",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            }
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Ошибка отправки промокода. Пустые данные пользователя",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        "Ошибка отправки промокода",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                }
            }
        }
    }

    fun pullToRefresh() {
        checkIsAdminUseCase.invoke { isAdmin ->
            if (isAdmin.data == true) {
                UserManager.setRole(Role.ADMIN)
                Log.d("UserManager", "${UserManager.getRole()}")
                isAuthedUserStorage.save(Role.ADMIN)
            }
        }
    }
}