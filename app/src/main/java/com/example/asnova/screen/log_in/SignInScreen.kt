package com.example.asnova.screen.log_in

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.asnova.R
import com.example.asnova.utils.toastMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import ru.ok.android.sdk.Odnoklassniki
import ru.ok.android.sdk.util.OkAuthType
import ru.ok.android.sdk.util.OkScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit,
    goProfile: () -> Unit
) {
    lateinit var authLauncher: ActivityResultLauncher<Collection<VKScope>>

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginDialog by remember {
        mutableStateOf(false)
    }
    var signUpDialog by remember {
        mutableStateOf(false)
    }

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
                Box(
                    modifier = Modifier
                        .size(450.dp, 60.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(percent = 10),
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        )
                        .background(Color(0xFF4C75A3))
                        .clip(RoundedCornerShape(10))
                        .clickable(onClick = {
                            // VK.login(context as Activity, arrayListOf())
                        })
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vklogo),
                        contentDescription = stringResource(R.string.vk_logo),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(48.dp)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        text = stringResource(R.string.vk_sign_in),
                        fontFamily = FontFamily(Font(R.font.pretendbold)),
                        color = Color(0xFFFFFFFF),
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // OK Sign-In Button
                Box(
                    modifier = Modifier
                        .size(450.dp, 60.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(percent = 10),
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        )
                        .background(Color(0xFFFF9900))
                        .clip(RoundedCornerShape(10))
                        .clickable(onClick = {
                            Odnoklassniki.createInstance(context, OK_APP_ID, OK_APP_KEY)
//                            Odnoklassniki
//                                .getInstance()
//                                .requestAuthorization(
//                                    context as Activity,
//                                    OK_REDIRECT_URI,
//                                    OkAuthType.ANY,
//                                    OkScope.VALUABLE_ACCESS,
//                                    OkScope.LONG_ACCESS_TOKEN
//                                )
                        })
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.oklogo),
                        contentDescription = stringResource(R.string.ok_logo),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(48.dp)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        text = stringResource(R.string.ok_sign_in),
                        fontFamily = FontFamily(Font(R.font.pretendbold)),
                        color = Color(0xFFFFFFFF),
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Email Sign-In Button
                Box(
                    modifier = Modifier
                        .size(450.dp, 60.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(percent = 10),
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        )
                        .background(Color(0xFFD0021B))
                        .clip(RoundedCornerShape(10))
                        .clickable(onClick = {
                            loginDialog = true
                        })
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        tint = Color(0xFFFBEFF1),
                        contentDescription = stringResource(R.string.email_icon),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(32.dp)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        text = stringResource(R.string.email_login),
                        fontFamily = FontFamily(Font(R.font.pretendbold)),
                        color = Color(0xFFFBEFF1),
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        if (loginDialog) {
            Dialog(
                onDismissRequest = { loginDialog = !loginDialog },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = Color(0xFFFFD900),
                            modifier = Modifier
                                .padding(12.dp, 0.dp)
                                .size(40.dp)
                                .align(Alignment.CenterStart),
                            contentDescription = stringResource(R.string.back)
                        )
                        Text(
                            text = stringResource(R.string.email_login),
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 25.2.sp,
                                fontFamily = FontFamily(Font(R.font.pretendbold)),
                                fontWeight = FontWeight(700),
                                color = Color(0xFF141414),
                                letterSpacing = 0.36.sp,
                            ),
                            modifier = Modifier
                                .padding(64.dp, 0.dp)
                                .align(Alignment.CenterStart)
                        )
                        Divider(
                            thickness = 1.dp, color = Color.Gray.copy(0.3f), modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(text = stringResource(R.string.email)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xFFFBFBFB),
                                focusedTextColor = Color(0xFF141414),
                                unfocusedTextColor = Color(0xFF141414),
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password input field
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(text = stringResource(R.string.password)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xFFFBFBFB),
                                focusedTextColor = Color(0xFF141414),
                                unfocusedTextColor = Color(0xFF141414),
                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .width(360.dp)
                                .height(48.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(percent = 10),
                                    spotColor = Color.Black,
                                    ambientColor = Color.Black
                                )
                                .background(Color(0xFFFFD900))
                                .clip(RoundedCornerShape(10))
                                .clickable {
                                    if (email == "" || password == "") {
                                        toastMessage(
                                            context,
                                            context.getString(R.string.empty_spaces_error)
                                        )
                                    } else {
                                        signInEmail(
                                            email,
                                            password,
                                            context,
                                            goProfile = goProfile
                                        )
                                    }
                                }
                        ) {
                            Text(
                                text = stringResource(R.string.sign_in),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.pretendbold)),
                                color = Color(0xFF141414),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.first_time_here),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.pretendardlight)),
                                color = Color.Gray,
                                modifier = Modifier.clickable { signUpDialog = !signUpDialog }
                            )
                            Divider(
                                thickness = 1.dp, modifier = Modifier
                                    .padding(horizontal = 40.dp)
                                    .size(1.dp, 20.dp)
                            )
                            Text(
                                text = stringResource(R.string.forgot_password),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.pretendardlight)),
                                color = Color.Gray,
                                modifier = Modifier.clickable { }
                            )
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
        if (signUpDialog) {
            Dialog(
                onDismissRequest = { signUpDialog = !signUpDialog },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = Color(0xFFFFD900),
                            modifier = Modifier
                                .padding(12.dp, 0.dp)
                                .size(40.dp)
                                .align(Alignment.CenterStart),
                            contentDescription = stringResource(R.string.back)
                        )
                        Text(
                            text = stringResource(R.string.sign_up),
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 25.2.sp,
                                fontFamily = FontFamily(Font(R.font.pretendbold)),
                                fontWeight = FontWeight(700),
                                color = Color(0xFF141414),
                                letterSpacing = 0.36.sp,
                            ),
                            modifier = Modifier
                                .padding(64.dp, 0.dp)
                                .align(Alignment.CenterStart)
                        )
                        Divider(
                            thickness = 1.dp, color = Color.Gray.copy(0.3f), modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        )
                    }
                    SignUpScreen(context = context, signUpDone = {
                        signUpDialog = false
                        Toast.makeText(
                            context,
                            context.getString(R.string.complete_registration),
                            Toast.LENGTH_LONG
                        ).show()
                    })
                }
            }
        }
    }
}

fun signInEmail(email: String, password: String, context: Context, goProfile: () -> Unit) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toastMessage(context, "Successful login")
                goProfile()
            } else {
                // Execute on login failure
                val errorCode = (task.exception as FirebaseAuthException).errorCode
                val errorMessage = task.exception?.message
                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
}

private const val OK_APP_ID = "125497344"
private const val OK_APP_KEY = "CBABPLHIABABABABA"
private const val OK_REDIRECT_URI = "okauth://ok125497344"
