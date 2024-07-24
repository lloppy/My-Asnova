package com.example.asnova

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asnova.storage.KEY_USER_SETTING
import com.asnova.storage.SHARED_PREFS_USER_SETTING
import com.example.asnova.data.UserManager
import com.example.asnova.navigation.Screen
import com.example.asnova.screen.log_in.LogInViewModel
import com.example.asnova.screen.log_in.SignInResult
import com.example.asnova.screen.log_in.SignInScreen
import com.example.asnova.screen.log_in.services.GoogleAuthUiClient
import com.example.asnova.screen.main.MainScreen
import com.example.asnova.screen.splash.SplashScreen
import com.example.asnova.ui.theme.AsnovaTheme
import com.example.asnova.utils.LOG_IN
import com.example.asnova.utils.navigation.createExternalRouter
import com.example.asnova.utils.navigation.navigate
import com.example.asnova.utils.toastMessage
import com.vk.id.VKID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var googleAuthUiClient: GoogleAuthUiClient

    private lateinit var navController: NavHostController


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val userSharedPreferences =
            this.getSharedPreferences(SHARED_PREFS_USER_SETTING, Context.MODE_PRIVATE)
        val user = userSharedPreferences.getString(KEY_USER_SETTING, false.toString())
        UserManager.status = when (user) {
            true.toString() -> true
            false.toString() -> false
            else -> false
        }
        Log.e("login_info", "User is $user")

        super.onCreate(savedInstanceState)
        VKID.init(this)

        setContent {
            AsnovaTheme {
                navController = rememberNavController()
                val viewModel = viewModel<LogInViewModel>()


                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null) {
                        viewModel.onSignInResult(
                            SignInResult(
                                data = googleAuthUiClient.getSignedInUser(),
                                errorMessage = null
                            )
                        )
                        navController.navigate(Screen.Main.route)
                    } else {
                        Log.d(LOG_IN, "Not log in yet")
                    }
                }

                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navHostController = navController, route = Screen.LogIn.route)
                    }
                    composable(Screen.LogIn.route) {
                        val state by viewModel.state.collectAsStateWithLifecycle()
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if (result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )
                        //При успешном входе в систему вы попадаете на главный экран
                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if (state.isSignInSuccessful) {
                                toastMessage(applicationContext, "Вход в систему успешный")

                                navController.navigate(route = Screen.Main.route) {
                                    popUpTo(route = Screen.Main.route) {
                                        inclusive = true
                                    }
                                }
                                viewModel.resetState()
                            }
                        }

                        //Если не вошли в систему
                        if (!state.isSignInSuccessful) {
                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
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
                                }
                            )
                        }
                    }
                    composable(Screen.Main.route) {
                        MainScreen(
                            context = this@MainActivity,
                            lifecycleScope = lifecycleScope,
                            lifecycleOwner = this@MainActivity,
                            googleAuthUiClient = googleAuthUiClient,
                            router = createExternalRouter { screen, params ->
                                navController.navigate(screen, params)
                            }
                        )
                    }
                }
            }
        }
    }
}