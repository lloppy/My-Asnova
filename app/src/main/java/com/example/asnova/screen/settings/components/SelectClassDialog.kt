package com.example.asnova.screen.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.asnova.model.AsnovaStudentsClass

@Composable
fun SelectClassDialog(
    asnovaStudentsClass: AsnovaStudentsClass,
    onDismiss: () -> Unit,
    onSave: (AsnovaStudentsClass) -> Unit
) {
    var updatedName by remember { mutableStateOf(asnovaStudentsClass.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выбрать группу") },
        text = {
            Column {
                Text(
                    text = asnovaStudentsClass.name,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(asnovaStudentsClass)
                onDismiss()
            }) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}