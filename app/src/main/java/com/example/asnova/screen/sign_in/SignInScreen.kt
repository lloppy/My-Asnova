package com.example.asnova.screen.sign_in

import android.app.Activity
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.asnova.R
import com.example.asnova.data.UserManager
import com.example.asnova.ui.theme.darkLinear
import com.example.asnova.ui.theme.grayAsnova
import com.example.asnova.utils.OtpView

@Composable
fun SignInScreen(
    state: SignInState,
    context: Context,
    onSignInClick: () -> Unit,
    onSignInClickEmail: () -> Unit,
    goProfile: () -> Unit,
    viewModel: SignInScreenViewModel = hiltViewModel()
) {
    val showPhone by remember { mutableStateOf(false) }
    var showOtp by remember { mutableStateOf(false) }

    var mobile by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    darkLinear,
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

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            if (showPhone) {
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text(text = "+7") },
                    isError = mobile.length > 10,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        errorTextColor = Color.Gray,
                        focusedBorderColor = Color.Gray,
                        focusedTextColor = Color.Gray,
                        unfocusedTextColor = Color.Gray,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    if (mobile.length == 10) {
                        showOtp = !showOtp
                        viewModel.createUserWithPhone(mobile, context as Activity) {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Телефон должен состоять из 10 цифр",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }) {
                    Text(text = "Получить SMS - код")
                }
                Spacer(modifier = Modifier.height(32.dp))

                if (state.otpSent) {
                    Text(text = "Введите SMS-код", color = grayAsnova)
                    Spacer(modifier = Modifier.height(6.dp))
                    OtpView(otpText = otp) { otpValue ->
                        otp = otpValue
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        if (otp.isNotEmpty()) {
                            viewModel.signInWithOtp(otp) {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Пожалуйста, введите код", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                        Text(text = "Подтвердить")
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            LaunchedEffect(UserManager.user) {
                if (UserManager.isUserSignedIn()) {
                    goProfile()
                }
            }

            if (!showPhone) {
                // Google Sign-In Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
