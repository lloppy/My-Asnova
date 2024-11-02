package com.example.asnova.screen.settings.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.asnova.screen.settings.SettingsScreenViewModel

@Composable
fun EnterPromocodeScreen(
    context: Context,
    navController: NavController,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state

    var promocode by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Введите промокод",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = promocode,
            onValueChange = { promocode = it },
            label = { Text("Промокод") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.submitPromocode(promocode.text, context, navController)
                promocode = TextFieldValue("")
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Text(text = "Отправить")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}