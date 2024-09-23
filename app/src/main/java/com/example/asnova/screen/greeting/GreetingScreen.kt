package com.example.asnova.screen.greeting

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.asnova.model.Role
import com.example.asnova.R
import com.example.asnova.navigation.Screen

@Composable
fun GreetingScreen(
    viewModel: GreetingScreenViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state by viewModel.state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF282C27),
                        Color(0xFF181A18),
                        Color(0xFF1A1C1A),
                        Color(0xFF1D1F1E),
                        Color(0xFF18231B)
                    ),
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
                    RoleButton(roleName = Role.WORKER, R.drawable.worker) {
                        viewModel.onRoleSelected(Role.WORKER) {
                            navHostController.navigate(Screen.LogIn.route)
                        }
                    }

                    RoleButton(roleName = Role.STUDENT, R.drawable.student) {
                        viewModel.onRoleSelected(Role.STUDENT) {
                            navHostController.navigate(Screen.LogIn.route)
                        }
                    }

                    RoleButton(roleName = Role.VISITOR, R.drawable.guest) {
                        viewModel.onRoleSelected(Role.VISITOR){
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoleButton(roleName: String, iconId: Int, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(75.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
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