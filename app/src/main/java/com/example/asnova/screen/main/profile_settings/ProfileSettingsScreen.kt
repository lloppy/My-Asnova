package com.example.asnova.screen.main.profile_settings

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import com.example.asnova.utils.navigation.Router

@Composable
fun ProfileSettingsScreen(
    externalRouter: Router,
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    lifecycleOwner: LifecycleOwner,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    Text(text = "ProfileSettingsScreen")

}