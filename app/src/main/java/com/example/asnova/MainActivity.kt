package com.example.asnova

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asnova.navigation.Screen
import com.example.asnova.navigation.SetupNavGraph
import com.example.asnova.screen.log_in.GoogleAuthUiClient
import com.example.asnova.ui.theme.AsnovaTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    val hideList = setOf(
        Screen.LogIn.route
    )

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsnovaTheme {
                navController = rememberNavController()
                val screen = navController.currentBackStackEntryAsState().value
                val showBottomBar = screen?.destination?.route !in hideList

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            SetupNavGraph(navController)
                        }
                    }) {}
            }
        }
    }
}
