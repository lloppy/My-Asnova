package com.example.asnova.screen.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Portrait
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.asnova.model.Resource
import com.asnova.model.Role
import com.asnova.model.User
import com.example.asnova.R
import com.example.asnova.data.UserManager
import com.example.asnova.screen.settings.components.SettingsItemBox
import com.example.asnova.screen.settings.components.UserEditInfoModalSheet
import com.example.asnova.ui.theme.BottomBarHeight
import com.example.asnova.ui.theme.blackShadesLinear
import com.example.asnova.utils.Router
import com.example.asnova.utils.SkeletonScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSettingsScreen(
    externalRouter: Router,
    context: Context,
    lifecycleScope: LifecycleCoroutineScope,
    lifecycleOwner: LifecycleOwner,
    navigateToChats: () -> Unit,
    navigateToChangeGroup: () -> Unit,
    navigateToSelectClass: () -> Unit,
    navigateToEnterPromocode: () -> Unit,
    onRestartApp: () -> Unit,
    navController: NavController,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    var userData by remember { mutableStateOf<User?>(null) }
    val state = viewModel.state

    var showBottomSheet by remember { mutableStateOf(false) }

    // Refresh state
    var isRefreshing by remember { mutableStateOf(false) }
    val stateRefresh = rememberPullRefreshState(isRefreshing, { viewModel.pullToRefresh() })

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = BottomBarHeight)
            .pullRefresh(stateRefresh)
    ) {
        SkeletonScreen(
            isLoading = state.value.loading,
            skeleton = {}
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (showBottomSheet) {
                    UserEditInfoModalSheet(
                        viewModel = viewModel,
                        showBottomSheet = showBottomSheet,
                        onDismiss = { showBottomSheet = false },
                        onSubmit = { name, surname, email, phone ->
                            viewModel.writeNewDataUser(name, surname, email, phone,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Данные успешно сохранены",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onFailure = { errorMessage ->
                                    Toast.makeText(
                                        context,
                                        "Ошибка: $errorMessage",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    )
                }
            }
            LazyColumn(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // информация профиля
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(0.dp))
                            .paint(
                                painterResource(id = R.drawable.asnova_future_gen),
                                contentScale = ContentScale.Crop,
                            )
                            .background(blackShadesLinear)
                            .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))

                        if (userData?.profilePictureUrl != null) {
                            AsyncImage(
                                model = userData!!.profilePictureUrl,
                                contentDescription = stringResource(R.string.profile_picture),
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        if (!userData?.surname.isNullOrEmpty() && !userData?.name.isNullOrEmpty()){
                            Text(
                                text =  userData!!.name + " " + userData!!.surname,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (!userData?.asnovaClass.isNullOrEmpty()){
                                Text(
                                    text = "Учебная группа: " + userData!!.asnovaClass,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                        } else if (userData?.username != null) {
                            Text(
                                text = userData!!.username!!,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (!userData?.asnovaClass.isNullOrEmpty()){
                                Text(
                                    text = "Учебная группа: " + userData!!.asnovaClass,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                // для админа редактировать группы Аснова
                item {
                    if (viewModel.canLoadAdminAccess()) {
                        SettingsItemBox(
                            icon = Icons.Outlined.Group,
                            text = "Редактировать учебные группы",
                            onClick = navigateToSelectClass
                        )
                    }
                }

                item {
                    if (UserManager.getRole() == Role.STUDENT || UserManager.getRole() == Role.ADMIN)  {
                        SettingsItemBox(
                            icon = Icons.Filled.Person,
                            text = "Редактировать информацию профиля",
                            onClick = {
                                showBottomSheet = true
                            }
                        )
                    }
                }

                item {
                    if (UserManager.getRole() == Role.STUDENT || UserManager.getRole() == Role.ADMIN) {
                        SettingsItemBox(
                            icon = Icons.Filled.Group,
                            text = "Сменить мою учебную группу",
                            onClick = navigateToChangeGroup
                        )
                    }
                }

                item {
                    if (UserManager.getRole() != Role.GUEST && UserManager.getRole() != Role.NONE) {
                        SettingsItemBox(
                            icon = Icons.Outlined.Discount,
                            text = "Ввести промокод",
                            onClick = navigateToEnterPromocode
                        )
                    }
                }

                // кнопка выйти
                item {
                    if (UserManager.getRole() != Role.GUEST && UserManager.getRole() != Role.NONE) {
                        SettingsItemBox(
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            text = stringResource(R.string.log_out),
                            onClick = {
                                lifecycleScope.launch {
                                    viewModel.signOut()
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }

                item {
                    if (UserManager.getRole() != Role.GUEST && UserManager.getRole() != Role.NONE) {
                        SettingsItemBox(
                            icon = Icons.Filled.DeleteOutline,
                            text = "Удалить аккаунт",
                            onClick = {
                                lifecycleScope.launch {
                                    viewModel.deleteAccount(context)
                                    viewModel.signOut()
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }

                item {
                    if (UserManager.getRole() == Role.NONE) {
                        SettingsItemBox(
                            icon = Icons.Filled.Replay,
                            text = "Перезапустить приложение и войти заново",
                            onClick = {
                                onRestartApp.invoke()
                            }
                        )
                    }
                }
            }
            PullRefreshIndicator(isRefreshing, stateRefresh, Modifier.align(Alignment.TopCenter))
        }
    }
}
