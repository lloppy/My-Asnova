package com.example.asnova.screen.greeting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GreetingScreenViewModel @Inject constructor(

) : ViewModel() {

    private val _state = mutableStateOf(GreetingState())
    val state: State<GreetingState> get() = _state

    fun onRoleSelected(role: String) {
        // Логика обработки выбора роли (например, переход на другой экран)
        _state.value = _state.value.copy(selectedRole = role)
    }
}
