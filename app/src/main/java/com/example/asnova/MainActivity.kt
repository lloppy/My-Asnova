package com.example.asnova

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asnova.navigation.Screen
import com.example.asnova.navigation.SetupNavGraph
import com.example.asnova.screen.log_in.GoogleAuthUiClient
import com.example.asnova.screen.main.MainScreen
import com.example.asnova.screen.splash.SplashScreen
import com.example.asnova.ui.theme.AsnovaTheme
import com.example.asnova.utils.navigation.createExternalRouter
import com.example.asnova.utils.navigation.navigate
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsnovaTheme {
                navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navHostController = navController, route = Screen.Main.route)
                    }
                    composable(Screen.Main.route) {
                        MainScreen(
                            context = this@MainActivity,
                            lifecycleScope = lifecycleScope,
                            lifecycleOwner = this@MainActivity,
                            router = createExternalRouter { screen, params ->
                                navController.navigate(screen, params)
                            })
                    }
                }
            }
        }
    }
}
