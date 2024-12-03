package com.example.asnova.screen.greeting.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asnova.model.Role
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginModalSheet(
    role: String,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    onSignInGoogle: () -> Unit,
    context: Context,
    onSubmit: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = if (role != Role.NONE) "Войти как " + role.lowercase(Locale.ROOT) else "Вход", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = it.isEmpty()
                        },
                        label = { Text("Электронная почта") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        isError = emailError
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = it.isEmpty()
                        },
                        label = { Text("Пароль") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        isError = passwordError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            emailError = email.isEmpty()
                            passwordError = password.isEmpty()

                            if ( !emailError && !passwordError) {
                                onSubmit(email, password)
                                onDismiss()
                            }
                        }) {
                        Text("Войти")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSignInGoogle.invoke()
                        },
                        colors = ButtonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text("или войти через Google")
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Ознакомиться с Политикой конфиденциальности",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.termsfeed.com/live/2ef6f1ab-c17b-42d2-b2df-80dd0218b31a"))
                                context.startActivity(intent)
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        )
    }
}