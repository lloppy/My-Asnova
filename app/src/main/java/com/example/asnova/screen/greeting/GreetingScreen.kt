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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asnova.model.Role
import com.example.asnova.R

@Composable
fun GreetingScreen(
    viewModel: GreetingScreenViewModel = hiltViewModel()
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
                .padding(top = 40.dp, start = 24.dp, end = 16.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Column(verticalArrangement = Arrangement.Top){
                Text(
                    text = "Добро",
                    fontFamily = FontFamily(
                        Font(
                            DeviceFontFamilyName("sans-serif-condensed"),
                            weight = FontWeight.SemiBold
                        )
                    ),
                    lineHeight = 1.sp,
                    color = Color.White,
                    fontSize = 48.sp,
                )
                Text(
                    text = "пожаловать в",
                    fontFamily = FontFamily(
                        Font(
                            DeviceFontFamilyName("sans-serif-condensed"),
                            weight = FontWeight.SemiBold
                        )
                    ),
                    lineHeight = 1.sp,
                    color = Color.White,
                    fontSize = 48.sp,
                )
                Text(
                    text = "УЭЦ АСНОВА!",
                    fontFamily = FontFamily(
                        Font(
                            DeviceFontFamilyName("sans-serif-condensed"),
                            weight = FontWeight.SemiBold
                        )
                    ),
                    lineHeight = 1.sp,
                    fontSize = 48.sp,
                    color = Color(0xFF80F988),
                )
            }
            Spacer(modifier = Modifier.height(80.dp))

            Column {
                Text(
                    text = "Выберите свою роль:",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.82f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoleButton(roleName = Role.WORKER) {
                        viewModel.onRoleSelected(Role.WORKER)
                    }

                    RoleButton(
                        roleName = Role.STUDENT,
                    ) {
                        viewModel.onRoleSelected(Role.STUDENT)
                    }

                    RoleButton(roleName = Role.VISITOR) {
                        viewModel.onRoleSelected(Role.VISITOR)
                    }
                }
            }
        }
    }
}

@Composable
fun RoleButton(roleName: String, onClick: () -> Unit) {
    Column {

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .padding(vertical = 8.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_schedule),
                contentDescription = roleName
            )
        }

        Text(
            text = roleName,
            color = Color.White,
            fontSize = 15.sp,
        )
    }
}