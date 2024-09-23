package com.example.asnova.screen.log_in

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asnova.R
import com.example.asnova.ui.theme.greenAsnova
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    goProfile: () -> Unit,
    goOtp: () -> Unit
) {
    val bottomSheetState = rememberOneTapBottomSheetState()

    LaunchedEffect(Unit) {
        delay(15000)
        bottomSheetState.show()
    }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Log.e("login_info", "$error! Вход не выполнен")
            onSignInClick.invoke()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF282C27),
                        Color(0xFF181A18),
                        Color(0xFF1A1C1A),
                        Color(0xFF1D1F1E),
                        Color(0xFF18231B)
                    ),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_white_background),
                contentDescription = null,
                alignment = Alignment.BottomStart,
                modifier = Modifier.fillMaxSize(0.9f),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Google Sign-In Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(percent = 12),
                        spotColor = Color.Black,
                        ambientColor = Color.Black
                    )
                    .background(Color.White)
                    .clip(RoundedCornerShape(12))
                    .clickable(onClick = onSignInClick)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.googlelogo),
                    contentDescription = stringResource(R.string.google_logo),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(48.dp)
                        .align(Alignment.CenterStart)
                )
                Text(
                    text = stringResource(R.string.google_sign_in),
                    fontFamily = FontFamily(Font(R.font.pretendbold)),
                    color = Color(0xFF1F1F1F),
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // VK Sign-In Button

            // не работает, потому что нужно выложить приложение в плей маркет
            // doesnt work - info here: https://id.vk.com/about/business/go/docs/ru/vkid/latest/oauth/oauth-mail/configure/application-settings#Obshie-nastrojki

            OAuthListWidget(
                onFail = { oAuth, fail ->
                    Log.e("AuthFail", "Error: ${fail.description}")
                },
                onAuth = { oAuth, token ->
                    Log.d("Auth", "Token name: ${token.userData.email}")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // https://id.vk.com/about/business/go/docs/ru/vkid/latest/vk-id/connection/android/floating-onetap
            OneTapBottomSheet(
                state = bottomSheetState,
                onAuth = { oAuth, token ->
                    Log.d("Auth", "Token name: ${token.userData.email}")
                },
                serviceName = stringResource(R.string.service_name)
            )

//            Spacer(modifier = Modifier.height(40.dp))
//
//            TextButton(onClick = { goOtp.invoke() }) {
//                Text(
//                    text = stringResource(R.string.login_using_phone),
//                    color = greenAsnova,
//                    fontSize = 12.sp
//                )
//            }
//            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}