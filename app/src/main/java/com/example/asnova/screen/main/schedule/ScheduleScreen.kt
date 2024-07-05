package com.example.asnova.screen.main.schedule

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.example.asnova.utils.navigation.Router


@Composable
fun ScheduleScreen(
    externalRouter: Router,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    viewModel: ScheduleScreenViewModel = hiltViewModel()
) {
    Text(text = "ScheduleScreen")
}