package com.example.asnova.screen.main.schedule.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.asnova.ui.theme.darkAsnova
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
            tint = darkAsnova,
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
                currentDate = currentDate,
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