package com.example.asnova

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asnova.model.Role
import com.example.asnova.data.UserManager
import com.example.asnova.navigation.Screen
import com.example.asnova.screen.greeting.GreetingScreen
import com.example.asnova.screen.main.MainScreen
import com.example.asnova.screen.sign_in.SignInScreenViewModel
import com.example.asnova.ui.theme.AsnovaTheme
import com.example.asnova.utils.createExternalRouter
import com.example.asnova.utils.navigate
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()
        setupFirebaseMessaging()

        setContent {
            AsnovaTheme {
                navController = rememberNavController()
                val viewModel: SignInScreenViewModel = hiltViewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                viewModel.signInWithIntent(
                                    intent = result.data ?: return@launch,
                                    role = UserManager.getRole(),
                                    fmc = UserManager.fmc
                                )
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful || UserManager.getRole() == Role.GUEST) {
                        navController.navigate(route = Screen.Main.route) {
                            popUpTo(route = Screen.Main.route) {
                                inclusive = true
                            }
                        }
                        viewModel.resetState()
                    }
                }

                NavHost(navController = navController, startDestination = Screen.Greeting.route) {
                    composable(Screen.Greeting.route) {
                        GreetingScreen(
                            isLoading = state.loading,
                            fmc = UserManager.fmc,
                            context = this@MainActivity,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = viewModel.signInWithLauncher()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            },
                            navHostController = navController,
                            signInViewModel = viewModel
                        )
                    }
                    composable(Screen.Main.route) {
                        MainScreen(
                            context = this@MainActivity,
                            lifecycleScope = lifecycleScope,
                            lifecycleOwner = this@MainActivity,
                            onRestartApp = { restartApp() },
                            router = createExternalRouter { screen, params ->
                                navController.navigate(screen, params)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setupFirebaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("token_fcm", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result ?: return@addOnCompleteListener

            Log.d("token_fcm", getString(R.string.msg_token_fmt, token))
            UserManager.fmc = token
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
        finish()
        Runtime.getRuntime().exit(0)
    }
}