package com.example.asnova

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.asnova.navigation.Screen
import com.example.asnova.screen.log_in.LogInViewModel
import com.example.asnova.screen.log_in.OtpScreen
import com.example.asnova.screen.log_in.SignInScreen
import com.example.asnova.screen.main.MainScreen
import com.example.asnova.screen.splash.SplashScreen
import com.example.asnova.ui.theme.AsnovaTheme
import com.example.asnova.utils.navigation.createExternalRouter
import com.example.asnova.utils.navigation.navigate
import com.vk.id.VKID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.init(this)

        setContent {
            AsnovaTheme {
                navController = rememberNavController()
                val viewModel: LogInViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = viewModel.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        navController.navigate(route = Screen.Main.route) {
                            popUpTo(route = Screen.Main.route) {
                                inclusive = true
                            }
                        }
                        viewModel.resetState()
                    }
                }

                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navHostController = navController, route = Screen.LogIn.route)
                    }
                    composable(Screen.LogIn.route) {
                        SignInScreen(
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = viewModel.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            },
                            goProfile = {
                                navController.navigate(route = Screen.Main.route) {
                                    popUpTo(route = Screen.Main.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            goOtp = {
                                navController.navigate(route = Screen.Otp.route)
                            }
                        )
                    }
                    composable(Screen.Main.route) {
                        MainScreen(
                            context = this@MainActivity,
                            lifecycleScope = lifecycleScope,
                            lifecycleOwner = this@MainActivity,
                            router = createExternalRouter { screen, params ->
                                navController.navigate(screen, params)
                            }
                        )
                    }
                    composable(Screen.Otp.route) {
                        OtpScreen(state = state)
                    }
                }
            }
        }
    }
}