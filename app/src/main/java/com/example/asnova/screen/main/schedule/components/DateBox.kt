package com.example.asnova.screen.main.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.asnova.ui.theme.darkAsnova
import com.example.asnova.ui.theme.darkGreenAsnova
import com.example.asnova.utils.getDayOfWeekInRussian
import java.time.LocalDate

@Composable
fun DateBox(
    date: LocalDate,
    currentDate: LocalDate,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(top = 12.dp)
            .clickable {
                onDateSelected(date)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = getDayOfWeekInRussian(date),
            color = darkAsnova,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(color = if (isSelected) darkGreenAsnova else Color.Transparent)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                modifier = Modifier.fillMaxSize(),
                color = if (isSelected) Color.White else darkAsnova,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview
@Composable
fun prevDateBox() {
    DateBox(
        date = LocalDate.now().minusDays(1),
        currentDate = LocalDate.now(),
        isSelected = true,
        onDateSelected = {}
    )
}