package com.example.asnova.screen.main.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.asnova.ui.theme.darkAsnova
import com.example.asnova.ui.theme.darkGreenAsnova
import com.example.asnova.utils.getDayOfWeekInRussian
import java.time.LocalDate

@Composable
fun WeekNavigationRow(
    lastMonday: MutableState<LocalDate>,
    dateList: MutableState<List<LocalDate>>,
    currentDate: LocalDate,
    selectedMutableDate: MutableState<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            tint = Color.Gray,
            contentDescription = "Previous Week",
            modifier = Modifier
                .clickable {
                    lastMonday.value = lastMonday.value.minusWeeks(1)
                    dateList.value = List(7) { index -> lastMonday.value.plusDays(index.toLong()) }
                }
                .padding(start = 8.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        )

        dateList.value.forEach { date ->
            DateBox(
                date = date,
                isSelected = (date == selectedMutableDate.value),
                modifier = Modifier.weight(1f),
                onDateSelected = { selectedDate ->
                    selectedMutableDate.value = selectedDate
                    onDateSelected(selectedDate)
                }
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            tint = Color.Gray,
            contentDescription = "Next Week",
            modifier = Modifier
                .clickable {
                    lastMonday.value = lastMonday.value.plusWeeks(1)
                    dateList.value = List(7) { index -> lastMonday.value.plusDays(index.toLong()) }
                }
                .padding(start = 0.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun DateBox(
    date: LocalDate,
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