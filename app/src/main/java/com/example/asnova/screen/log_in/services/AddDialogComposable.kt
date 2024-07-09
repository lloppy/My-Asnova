package com.example.asnova.screen.log_in.services

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vk.api.sdk.VK
import ru.ok.android.sdk.Odnoklassniki
import ru.ok.android.sdk.util.OkAuthType
import ru.ok.android.sdk.util.OkScope

@Composable
fun AddDialogComposable() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Add social network",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        Toast.makeText(context, "Selected: Odnoklassniki", Toast.LENGTH_SHORT).show()

                        showDialog = false
                    }) {
                        Text(text = "Odnoklassniki")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        Toast.makeText(context, "Selected: Vkontakte", Toast.LENGTH_SHORT).show()
                        showDialog = false
                    }) {
                        Text(text = "Vkontakte")
                    }
                }
            }
        }
    }
}


