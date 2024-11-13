package com.example.asnova.screen.settings.components.admin_classes.command

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.asnova.domain.usecase.CleanAsnovaClassesFromFirebaseUseCase
import com.asnova.domain.usecase.GetAsnovaClassesFromFirebaseUseCase
import com.asnova.domain.usecase.GetRawAsnovaClassesUseCase
import com.asnova.domain.usecase.PushAsnovaClassesUseCase
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.example.asnova.screen.settings.SettingsState
import javax.inject.Inject

class ClassesViewModel @Inject constructor(

    private val pushAsnovaClassesUseCase: PushAsnovaClassesUseCase,

    private val getAsnovaClassesFromFirebaseUseCase: GetAsnovaClassesFromFirebaseUseCase,
    private val getRawAsnovaClassesUseCase: GetRawAsnovaClassesUseCase,

    private val cleanAsnovaClassesFromFirebaseUseCase: CleanAsnovaClassesFromFirebaseUseCase

) : ViewModel(), CommandReceiver {

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    override fun onDeleteClicked() {
        TODO("Not yet implemented")
    }

    override fun onDeleteAllClicked(callback: (Boolean) -> Unit) {
        cleanAsnovaClassesFromFirebaseUseCase.invoke { result ->
            when (result) {
                true -> {
                    callback(true)
                }

                false -> {
                    Log.e("CleanDatabase", "Error cleaning database")
                    callback(false)
                }
            }
        }
    }

    override fun onGetRawClicked(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        Log.d("studentsClasses", "Fetching Raw Asnova classes...")

        getRawAsnovaClassesUseCase.invoke(callback = { result ->
            Log.d("studentsClasses", "Received Raw result from use case")
            handleAsnovaClassesResult(result)
        })
    }

    override fun onGetFirebaseClicked(callback: (Resource<List<AsnovaStudentsClass>>) -> Unit) {
        Log.d("studentsClasses", "Fetching Asnova classes...")

        getAsnovaClassesFromFirebaseUseCase.invoke(callback = { result ->
            Log.d("studentsClasses", "Received result from use case")
            handleAsnovaClassesResult(result)
            callback(result)
        })
    }

    override fun onSaveClicked(
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
}