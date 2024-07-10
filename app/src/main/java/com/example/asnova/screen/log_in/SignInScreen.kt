package com.example.asnova.screen.log_in

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asnova.R
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    goProfile: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                tween(2200, easing = FastOutSlowInEasing),
                initialOffsetY = { it / 8 }) + fadeIn(tween(2200, easing = FastOutSlowInEasing))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(240.dp))

                // Google Sign-In Button
                Box(
                    modifier = Modifier
                        .size(450.dp, 60.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(percent = 10),
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        )
                        .background(Color(0xFFF2F2F2))
                        .clip(RoundedCornerShape(10))
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
                val bottomSheetState = rememberOneTapBottomSheetState()
                OAuthListWidget(
                    onFail = { oAuth, fail ->
                        Log.e("AuthFail", "Error: ${fail.description}")
                    },
                    onAuth = { oAuth, token ->
                        Log.d("Auth", "Token name: ${token.userData.email}")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OneTapBottomSheet(
                    state = bottomSheetState,
                    onAuth = { oAuth, token ->
                        // Использование токена
                        token.userData.email

                    },
                    serviceName = "Cool app"
                )
                Spacer(modifier = Modifier.height(16.dp))

                OneTap(
                    onAuth = { oAuth, token ->
                        // Использование токена
                        token.userData.email

                    },
                    signInAnotherAccountButtonEnabled = true
                )
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}