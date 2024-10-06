package com.example.asnova.screen.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.asnova.model.Resource
import com.asnova.model.User
import com.example.asnova.R
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.asnova.utils.Router
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    externalRouter: Router,
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    lifecycleOwner: LifecycleOwner,
    navigateToChats: () -> Unit,
    navigateToSelectClass: () -> Unit,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    var userData by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getUserData { resource ->
            when (resource) {
                is Resource.Success -> {
                    userData = resource.data
                }

                is Resource.Error -> {
                    // Handle error
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile_settings),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navigateToChats.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ChatBubble,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LogInContent(
            padding = padding,
            viewModel = viewModel,
            userData = userData,
            navigateToSelectClass = navigateToSelectClass,
            onSignOut = {
                lifecycleScope.launch {
                    viewModel.signOut()
                }
            }
        )
    }
}

@Composable
fun LogInContent(
    padding: PaddingValues,
    userData: User?,
    viewModel: SettingsScreenViewModel,
    navigateToSelectClass: () -> Unit,
    onSignOut: () -> Unit
) {
    Box(
        modifier = Modifier.padding(top = padding.calculateTopPadding(), bottom = BottomBarHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = stringResource(R.string.profile_picture),
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (userData?.username != null) {
                    Text(
                        text = userData.username!!,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (viewModel.canLoadAdminAccess()) {
                        Button(onClick = navigateToSelectClass) {
                            Text(text = "Выбрать учебную группу")
                        }
                        Spacer(modifier = Modifier.height(36.dp))
                    }
                }
            }
            Button(
                onClick = onSignOut,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = stringResource(R.string.log_out),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard)),
                        fontWeight = FontWeight(600),
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}