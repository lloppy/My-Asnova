package com.example.asnova.screen.sign_in.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asnova.model.Resource
import com.example.asnova.R
import com.example.asnova.screen.sign_in.SignInScreenViewModel
import com.example.asnova.ui.theme.darkLinear


@Composable
fun EmailSignInScreen(
    context: Context,
    navController: NavController,
    onSignInClick: () -> Unit,
    viewModel: SignInScreenViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
        contentAlignment = Alignment.Center
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

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorTextColor = Color.Gray,
                    focusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorTextColor = Color.Gray,
                    focusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSignInClick
            ) {
                Text("Войти")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                viewModel.registerWithEmail(email, password) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            Toast.makeText(context, "Регистрация выполнена", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                context,
                                resource.message ?: "Ошибка",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                context,
                                resource.message ?: "Ошибка",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }) {
                Text("Зарегистрироваться")
            }
        }
    }
}