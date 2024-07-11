package com.example.asnova.screen.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asnova.navigation.BottomNavigationBar
import com.example.asnova.navigation.Screen
import com.example.asnova.navigation.SetupNavGraph
import com.example.asnova.screen.log_in.services.GoogleAuthUiClient
import com.example.asnova.utils.navigation.Router

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    lifecycleOwner: LifecycleOwner,
    router: Router,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val hideList = setOf(
        Screen.LogIn.route,
    )

    val navController = rememberNavController()
    val screen = navController.currentBackStackEntryAsState().value
    val showBottomBar = screen?.destination?.route !in hideList

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) {
        SetupNavGraph(
            navController,
            context = context,
            lifecycleScope = lifecycleScope,
            lifecycleOwner = lifecycleOwner,
            router = router,
            googleAuthUiClient = googleAuthUiClient
        )
    }
}
