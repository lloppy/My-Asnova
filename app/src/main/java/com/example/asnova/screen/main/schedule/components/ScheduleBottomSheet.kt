package com.example.asnova.screen.main.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asnova.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    schoolSchedule: com.asnova.model.AsnovaSchedule,
    onClickId: () -> Unit,
    onClickAC: () -> Unit,
    onClickAction: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() }) {
        Box(modifier = Modifier.fillMaxSize())
        {
            Column {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.task),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                    )
                    {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (schoolSchedule.task.id.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = stringResource(id = R.string.id))
                                    TextButton(onClick = {
                                        onClickId()
                                    }) {
                                        Text(schoolSchedule.task.id)
                                    }
                                }
                            }
                            if (schoolSchedule.task.accessCode.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = stringResource(id = R.string.access_code))
                                    TextButton(onClick = {
                                        onClickAC()
                                    }) {
                                        Text(schoolSchedule.task.accessCode)
                                    }
                                }
                            }
                            TextButton(onClick = {
                                onClickAction()
                            }) {
                                Text(stringResource(id = R.string.open) + " " + schoolSchedule.task.nameApp)
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.homework),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                LazyColumn {
                    itemsIndexed(schoolSchedule.homeWork) { _, item ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .fillMaxSize()
                                .background(
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                        )
                        {
                            Text(
                                text = item,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}