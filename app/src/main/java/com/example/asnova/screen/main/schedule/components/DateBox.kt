package com.example.asnova.screen.main.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.asnova.R
import com.example.asnova.ui.theme.darkAsnova
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateBox(
    date: LocalDate,
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val boxSize = 90.dp

    val boxModifier = Modifier
        .size(boxSize)
        .padding(start = 8.dp)
        .padding(top = 12.dp, bottom = 12.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(color = darkAsnova.copy(alpha = 0.9f))
        .clickable {
            onDateSelected(date)
        }

    Column(
        modifier = boxModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (date) {
                    currentDate -> stringResource(id = R.string.today)
                    currentDate.plusDays(1) -> stringResource(id = R.string.tomorrow)
                    else -> date.format(DateTimeFormatter.ofPattern("d MMMM"))
                },
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
