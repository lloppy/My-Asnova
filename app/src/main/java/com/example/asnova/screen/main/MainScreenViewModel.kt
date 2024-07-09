package com.example.asnova.screen.main

import androidx.lifecycle.ViewModel
import com.example.asnova.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
) : ViewModel() {
    private val _bottomItems = listOf(Screen.Feed, Screen.Schedule, Screen.ProfileSettings)
    val bottomItems = _bottomItems
}