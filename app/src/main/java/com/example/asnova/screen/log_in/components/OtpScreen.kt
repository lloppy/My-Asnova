package com.example.asnova.screen.log_in.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.asnova.screen.log_in.SignInScreenViewModel
import com.example.asnova.screen.log_in.SignInState
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
    state: SignInState,
    viewModel: SignInScreenViewModel = hiltViewModel()
) {
    var mobile by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun handleCreateUser() {
        scope.launch {
            try {
                isDialog = true
                val result = viewModel.createUserWithPhone(mobile)
                isDialog = false
                Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                isDialog = false
                Toast.makeText(context, e.message ?: "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun handleSignIn() {
        scope.launch {
            try {
                isDialog = true
              //  val result = viewModel.signInWithOtp(otp)
                isDialog = false

//                context.startActivity(Intent(context, nextActivity))
            } catch (e: Exception) {
                isDialog = false
                Toast.makeText(context, e.message ?: "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Enter Mobile Number")
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text(text = "+7") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = ::handleCreateUser) {
                Text(text = "Submit")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Enter OTP")
            Spacer(modifier = Modifier.height(20.dp))
            OtpView(otpText = otp) {
                otp = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = ::handleSignIn) {
                Text(text = "Verify")
            }
        }
    }

    // Display loading dialog if needed
    if (isDialog) {
        AlertDialog(
            onDismissRequest = { isDialog = false },
            title = { Text("Loading") },
            text = { Text("Please wait...") },
            confirmButton = {
                Button(onClick = { isDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
