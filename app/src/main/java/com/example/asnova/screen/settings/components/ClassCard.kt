package com.example.asnova.screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asnova.model.AsnovaStudentsClass


@Composable
fun ClassCard(
    asnovaClass: AsnovaStudentsClass,
    onClick: (AsnovaStudentsClass) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick.invoke(asnovaClass)
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = asnovaClass.name)
        }
    }
}