package com.example.asnova.screen.settings.components.admin_classes

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.example.asnova.screen.settings.SettingsScreenViewModel
import com.example.asnova.screen.settings.components.ClassCard
import com.example.asnova.screen.settings.components.admin_classes.command.ClassesViewModel
import com.example.asnova.screen.settings.components.admin_classes.command.Command
import com.example.asnova.screen.settings.components.admin_classes.command.GetFirebaseClassesCommand
import com.example.asnova.screen.settings.components.admin_classes.command.GetRawClassesCommand
import com.example.asnova.screen.settings.components.admin_classes.command.PushClassesToFirebaseCommand
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.asnova.ui.theme.backgroundAsnova
import com.example.asnova.utils.SkeletonScreen
import com.example.asnova.utils.shimmerEffect

@Composable
fun SelectClassScreen(
    context: Context,
    classesViewModel: ClassesViewModel = hiltViewModel(),
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state
    var studentsClasses by remember { mutableStateOf<List<AsnovaStudentsClass>?>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf<AsnovaStudentsClass?>(null) }

    var expanded by remember { mutableStateOf(false) }

    // Паттерн Command
    val commandProcessor = { command: Command ->
        classesViewModel.processCommand(command)
    }

    LaunchedEffect(Unit) {
        commandProcessor(GetFirebaseClassesCommand { resource ->
            when (resource) {
                is Resource.Success -> {
                    studentsClasses = resource.data
                    Log.e("studentsClasses", "${studentsClasses?.first()?.name}")
                }

                is Resource.Error -> {
                    Log.e("studentsClasses", "Error in getAsnovaClasses. " + resource.message)
                }

                is Resource.Loading -> {
                    Log.e("studentsClasses", "Loading...")
                }

                else -> {
                    Log.e("studentsClasses", "idk what happening...")
                }
            }
        })
    }

    Box(
        modifier = Modifier
            .background(backgroundAsnova)
            .padding(bottom = BottomBarHeight)
    ) {
        SkeletonScreen(
            isLoading = state.loading,
            skeleton = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    item {
                        Text(
                            text = "Выберите группу",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(5) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 12.dp)
                                .height(120.dp)
                                .clip(shape = MaterialTheme.shapes.medium)
                                .shimmerEffect()
                        )
                    }
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        Row(Modifier.align(Alignment.TopEnd)) {
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(
                                    text = { Text(text = "Получить список групп повторно (текущие изменения не сохранятся)") },
                                    onClick = {
                                        commandProcessor(GetFirebaseClassesCommand { resource ->
                                            if (resource is Resource.Success) {
                                                studentsClasses = resource.data
                                            }
                                        })
                                        expanded = false
                                    })

                                DropdownMenuItem(
                                    text = { Text(text = "Получить данные о группах заново (\"сырые\" данные из расписания)") },
                                    onClick = {
                                        commandProcessor(GetRawClassesCommand { resource ->
                                            if (resource is Resource.Success) {
                                                studentsClasses = resource.data
                                            }
                                        })
                                        expanded = false
                                    })

                                /*
                            DropdownMenuItem(
                                text = { Text(text = "Очистить всё на базе данных") },
                                onClick = {
                                    commandProcessor(GetRawClassesCommand { success ->
                                        if (success) {
                                            Toast.makeText(context, "База данных успешно очищена", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Ошибка при очистке базы данных", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }
                            )
                                 */

                                DropdownMenuItem(
                                    text = { Text("Сохранить текущие изменения, отправив новые данные") },
                                    onClick = {
                                        commandProcessor(PushClassesToFirebaseCommand(state.asnovaClasses) {
                                            Toast.makeText(
                                                context,
                                                "Успешно сохранено на базе данных",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        })
                                        expanded = false
                                    })
                            }
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Выберите группу",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Box(modifier = Modifier.clickable { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = searchQuery,
                        onValueChange = { newText -> searchQuery = newText },
                        label = { Text("Поиск группы") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        state.asnovaClasses?.filter {
                            it.name.contains(
                                searchQuery,
                                ignoreCase = true
                            )
                        }?.forEach { asnovaClass ->
                            ClassCard(asnovaClass = asnovaClass, onClickDelete = {
                                viewModel.removeClass(asnovaClass)
                            }) { selected ->
                                selectedClass = selected
                                showEditDialog = true
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        )

        if (showEditDialog && selectedClass != null) {
            EditClassDialog(
                asnovaStudentsClass = selectedClass!!,
                onDismiss = { showEditDialog = false },
                onSave = { updatedClass ->
                    viewModel.duplicateAsnovaClass(updatedClass)
                    showEditDialog = false
                }
            )
        }

        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
    }
}