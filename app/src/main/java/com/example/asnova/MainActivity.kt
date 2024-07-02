package com.example.asnova

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asnova.navigation.Route
import com.example.asnova.navigation.SetupNavGraph
import com.example.asnova.screen.log_in.GoogleAuthUiClient
import com.example.asnova.ui.theme.AsnovaTheme
import com.google.android.gms.auth.api.identity.Identity

class SharedViewModel : ViewModel() {
    var expandedCardId by mutableStateOf<String?>(null) // переменная для хранения идентификатора расширенной в данный момент
}

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val viewModel by viewModels<SharedViewModel>()
    val hideList = setOf(
        Route.LogIn.route
    )

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userSharedPreferences =
            this.getSharedPreferences(SHARED_PREFS_USER_SETTING, Context.MODE_PRIVATE)
        val user = userSharedPreferences.getString(KEY_USER_SETTING, false.toString())
        UserManager.status = when(user)
        {
            true.toString() -> true
            false.toString() -> false
            else -> false
        }
        setContent {
            AsnovaTheme {
                var showBottomBar by remember { mutableStateOf(true) }

                navController = rememberNavController()
                val screen = navController.currentBackStackEntryAsState().value
                LaunchedEffect(screen?.destination?.route) {
                    showBottomBar = screen?.destination?.route !in hideList
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            SetupNavGraph(navController, viewModel)
                        }
                    }) {}

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null) {
                        navController.navigate(Route.LogIn.route)
                    }
                }
            }
        }
    }
}
    
