//package com.example.asnova.screen.log_in.services
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.widget.Toast
//import com.example.asnova.screen.log_in.services.VKAuthenticationResult.*
//import com.vk.api.sdk.VK
//import com.vk.api.sdk.auth.VKAccessToken
//import com.vk.api.sdk.auth.VKScope
//import com.vk.api.sdk.auth.VKAuthCallback
//import com.vk.api.sdk.exceptions.VKAuthException
//import com.vk.api.sdk.ui.VKConfirmationActivity.Companion.result
//import ru.ok.android.sdk.Odnoklassniki
//import ru.ok.android.sdk.util.OkScope
//
//object SignInSocialNetwork {
//    fun signInWithVK(activity: Activity) {
//        VK.login(activity, listOf(VKScope.EMAIL))
//    }
//
//    fun handleVKResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context, goProfile: () -> Unit) {
//        val callback = object : VKAuthCallback {
//            override fun onLogin(token: VKAccessToken) {
//                when (result) {
//                    true -> {
//                        result = result as Success
//                        val email = result.email
//                        val userId = result.userId.toString()
//                        // Use Firebase or your backend to complete the sign-in process
//                        goProfile()
//                    }
//
//                    false -> {
//                        result = result as Failed
//
//                        Toast.makeText(context, "VK Sign-In Failed: ${result.exception.message}", Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//
//            override fun onLoginFailed(authException: VKAuthException) {
//                Toast.makeText(context, "VK Sign-In Failed: Error code $authException", Toast.LENGTH_LONG).show()
//            }
//        }
//        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
//            Toast.makeText(context, "VK Sign-In Failed", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    fun signInWithOK(activity: Activity) {
//        val odnoklassniki = Odnoklassniki.getInstance()
//        odnoklassniki.requestAuthorization(
//            activity, false,
//            OkScope.VALUABLE_ACCESS, OkScope.PHOTO_CONTENT
//        ) { result ->
//            if (result.isSuccess) {
//                // Handle successful OK authentication
//                val token = result.accessToken
//                // Use Firebase or your backend to complete the sign-in process
//                activity.runOnUiThread {
//                    goProfile()
//                }
//            } else {
//                // Handle failed OK authentication
//                activity.runOnUiThread {
//                    Toast.makeText(activity, "OK Sign-In Failed: ${result.errorMessage}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//}
//
//sealed class VKAuthenticationResult {
//    data class Success(val email: String?, val userId: String) : VKAuthenticationResult()
//    data class Failed(val exception: Exception) : VKAuthenticationResult()
//}
