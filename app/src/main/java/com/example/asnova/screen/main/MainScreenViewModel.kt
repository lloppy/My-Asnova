package com.example.asnova.screen.main

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.asnova.MainActivity
import com.example.asnova.navigation.Screen
import com.example.asnova.screen.log_in.GoogleAuthUiClient
import com.example.asnova.screen.log_in.IsNotLogin
import com.example.asnova.utils.LOG_IN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
) : ViewModel() {
    private val _bottomItems = listOf(Screen.Feed, Screen.Schedule, Screen.ProfileSettings)
    val bottomItems = _bottomItems
}