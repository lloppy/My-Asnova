package com.example.asnova.screen.main.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.asnova.screen.main.MainScreenViewModel
import com.example.asnova.ui.theme.grayAsnova

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoModalSheet(
    viewModel: MainScreenViewModel,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(viewModel.userData?.email ?: "") }
    var phone by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var surnameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

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
                    Text(text = "Введите ваши данные", style = MaterialTheme.typography.titleMedium)

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
                        enabled = viewModel.userData?.email.isNullOrEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = emailError
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = it.isEmpty()
                        },
                        label = { Text("Имя") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError
                    )

                    OutlinedTextField(
                        value = surname,
                        onValueChange = {
                            surname = it
                            surnameError = it.isEmpty()
                        },
                        label = { Text("Фамилия") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        isError = surnameError
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            phoneError = it.isEmpty()
                        },
                        label = { Text("Телефон") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Phone
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        isError = phoneError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            nameError = name.isEmpty()
                            surnameError = surname.isEmpty()
                            emailError = email.isEmpty()
                            phoneError = phone.isEmpty() || !phone.matches(Regex("\\+?[0-9]*"))

                            if (!nameError && !surnameError && !emailError && !phoneError) {
                                onSubmit(name, surname, email, phone)
                                onDismiss()
                            }
                        }) {
                        Text("Готово")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSubmit(
                                checkForNullOrEmpty(viewModel.userData?.name),
                                checkForNullOrEmpty(viewModel.userData?.surname),
                                checkForNullOrEmpty(viewModel.userData?.email),
                                checkForNullOrEmpty(viewModel.userData?.phone)
                            )
                            onDismiss()
                        },
                        colors = ButtonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text("Больше не спрашивать")
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        )
    }
}

private fun checkForNullOrEmpty(value: String?): String {
    return if (value.isNullOrEmpty()) "-" else value
}
