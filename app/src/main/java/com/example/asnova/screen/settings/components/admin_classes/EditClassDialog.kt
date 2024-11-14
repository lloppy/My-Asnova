package com.example.asnova.screen.settings.components.admin_classes

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
fun EditClassDialog(
    asnovaStudentsClass: AsnovaStudentsClass,
    onDismiss: () -> Unit,
    onSave: (AsnovaStudentsClass) -> Unit
) {
    var updatedName by remember { mutableStateOf(asnovaStudentsClass.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать группу") },
        text = {
            Column {
                TextField(
                    value = updatedName,
                    onValueChange = { updatedName = it },
                    label = { Text("Название группы") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val duplicatedClass = asnovaStudentsClass.clone().copy(name = updatedName)
                onSave(duplicatedClass)

                onDismiss()
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}