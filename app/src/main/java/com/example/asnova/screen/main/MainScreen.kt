package com.example.asnova.screen.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asnova.navigation.BottomNavigationBar
import com.example.asnova.navigation.Screen
import com.example.asnova.navigation.SetupNavGraph
import com.example.asnova.screen.main.components.UserInfoModalSheet
import com.example.asnova.utils.Router
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    lifecycleOwner: LifecycleOwner,
    router: Router,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val hideList = setOf(
        Screen.LogIn.route,
    )

    val navController = rememberNavController()
    val screen = navController.currentBackStackEntryAsState().value
    val showBottomBar = screen?.destination?.route !in hideList
    var showBottomSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(Unit) {
        viewModel.checkUserData { userDataExists ->
            if (!userDataExists) {
                showBottomSheet = true
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.padding(bottom = screenHeight.minus(160.dp))) },
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
            router = router
        )

        if (showBottomSheet) {
            UserInfoModalSheet(
                viewModel = viewModel,
                showBottomSheet = showBottomSheet,
                onDismiss = { showBottomSheet = false },
                onSubmit = { name, surname, email, phone ->
                    viewModel.writeNewDataUser(name, surname, email, phone,
                        onSuccess = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Данные успешно сохранены")
                            }
                        },
                        onFailure = { errorMessage ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Ошибка: $errorMessage")
                            }
                        }
                    )
                }
            )
        }
    }
}