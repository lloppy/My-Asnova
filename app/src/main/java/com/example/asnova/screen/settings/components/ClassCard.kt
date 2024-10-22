package com.example.asnova.screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.asnova.model.AsnovaStudentsClass


@Composable
fun ClassCard(
    asnovaClass: AsnovaStudentsClass,
    onClickDelete: (AsnovaStudentsClass) -> Unit,
    onClickEdit: (AsnovaStudentsClass) -> Unit

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClickEdit.invoke(asnovaClass)
            }
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = asnovaClass.name)

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    tint = Color.Gray,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onClickDelete.invoke(asnovaClass)
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}