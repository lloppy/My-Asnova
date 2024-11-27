package com.example.asnova.screen.greeting

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.asnova.model.Resource
import com.asnova.model.Role
import com.example.asnova.R
import com.example.asnova.navigation.Screen
import com.example.asnova.ui.theme.darkLinear

@Composable
fun GreetingScreen(
    isLoading: Boolean,
    onSignInClick: () -> Unit,
    context: Context,
    navHostController: NavHostController,
    viewModel: GreetingScreenViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    darkLinear,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_white_background),
                contentDescription = null,
                alignment = Alignment.BottomStart,
                modifier = Modifier.fillMaxSize(0.9f),
            )
        }

        LaunchedEffect(Unit) {
            if (!isNetworkAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show()
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, start = 26.dp, end = 16.dp, bottom = 60.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Text(
                    text = "Добро\nпожаловать в",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                )
                Text(
                    text = "УЭЦ АСНОВА!",
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF80F988),
                )
            }
            Spacer(modifier = Modifier.height(80.dp))

            Column {
                Text(
                    text = "Выберите свою роль:",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 24.dp, start = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoleButton(roleName = Role.WORKER, R.drawable.worker, isLoading) {
                        viewModel.onRoleSelected(Role.WORKER) {
                            onSignInClick.invoke()
                        }
                    }

                    RoleButton(roleName = Role.STUDENT, R.drawable.student, isLoading) {
                        viewModel.onRoleSelected(Role.STUDENT) {
                            onSignInClick.invoke()
                        }
                    }

                    RoleButton(roleName = Role.GUEST, R.drawable.guest, isLoading) {
                        viewModel.onRoleSelected(Role.GUEST){
                            navHostController.navigate(Screen.Main.route)
                        }
                    }
                }
            }
        }
    }
}

private fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

@Composable
fun RoleButton(roleName: String, iconId: Int, isLoading: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(75.dp),
            shape = CircleShape,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, disabledContainerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
        ) {
            Image(
                painter = painterResource(id = iconId),
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer(scaleX = 1.3f, scaleY = 1.3f),
                contentDescription = roleName
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = roleName,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}