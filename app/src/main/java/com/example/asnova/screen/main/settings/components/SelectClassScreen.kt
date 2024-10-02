package com.example.asnova.screen.main.settings.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Publish
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asnova.model.AsnovaStudentsClass
import com.asnova.model.Resource
import com.example.asnova.screen.main.settings.SettingsScreenViewModel
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.asnova.ui.theme.backgroundAsnova
import com.example.asnova.utils.SkeletonScreen
import com.example.asnova.utils.shimmerEffect

@Composable
fun SelectClassScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state
    var studentsClasses by remember { mutableStateOf<List<AsnovaStudentsClass>?>(emptyList()) }

    var searchQuery by remember { mutableStateOf("") }

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedClass by remember { mutableStateOf<AsnovaStudentsClass?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAsnovaClasses { resource ->
            when (resource) {
                is Resource.Success -> {
                    studentsClasses = resource.data
                    Log.e("studentsClasses", "${studentsClasses?.first()?.name}")
                }

                is Resource.Error -> {
                    // Handle error
                    Log.e("studentsClasses", "Error in getAsnovaClasses")
                }

                is Resource.Loading -> {
                    Log.e("studentsClasses", "Loading...")
                }

                else -> {
                    Log.e("studentsClasses", "idk what happening...")
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .background(backgroundAsnova)
            .padding(bottom = BottomBarHeight)
    ) {
        SkeletonScreen(
            isLoading = state.loading,
            skeleton = {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            text = "Выберите группу",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Divider(
                            modifier = Modifier
                                .height(2.dp)
                                .fillMaxWidth()
                        )
                    }
                    items(5) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
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
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.Sync,
                            tint = Color.Gray,
                            contentDescription = "Sync Asnova Classes",
                            modifier = Modifier
                                .clickable {
                                    viewModel.getAsnovaClasses { resource ->
                                        if (resource is Resource.Success) {
                                            studentsClasses = resource.data
                                        }
                                    }
                                }
                                .padding(8.dp)
                        )

                        Icon(
                            imageVector = Icons.Rounded.Download,
                            tint = Color.Gray,
                            contentDescription = "Get from Firebase",
                            modifier = Modifier
                                .clickable {
                                    viewModel.getAsnovaClasses { resource ->

                                    }
                                }
                                .padding(8.dp)
                        )

                        Icon(
                            imageVector = Icons.Rounded.Publish,
                            tint = Color.Gray,
                            contentDescription = "Publish into Firebase",
                            modifier = Modifier
                                .clickable {
                                    viewModel.getAsnovaClasses { resource ->

                                    }
                                }
                                .padding(8.dp)
                        )

                    }
                    Text(text = "Выберите группу", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = searchQuery,
                        onValueChange = { newText -> searchQuery = newText },
                        label = { Text("Поиск группы") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        state.asnovaClasses
                            ?.filter { it.name.contains(searchQuery, ignoreCase = true) }
                            ?.forEach { asnovaClass ->
                                ClassCard(asnovaClass) { selected ->
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
                    // Паттерн Prototype
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