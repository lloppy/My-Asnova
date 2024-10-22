package com.asnova.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.SignInResult
import com.asnova.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient
) : UserRepository {
    private var database: DatabaseReference = Firebase.database.reference
    private val _auth: FirebaseAuth = Firebase.auth

    override fun writeNewDataUser(
        name: String,
        surname: String,
        email: String,
        phone: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUid = currentUser?.uid
        if (userUid != null) {
            val user = User(
                userUid = userUid,
                username = currentUser.displayName,
                name = name,
                surname = surname,
                email = currentUser.email,
                phone = phone,
                profilePictureUrl = currentUser.photoUrl?.toString()
            )

            database.child("users").child(userUid).setValue(user)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Unknown error")
                    Log.e("UserRepository", "Error writing user data", exception)
                }
        } else {
            onFailure("User not authenticated")
        }
    }

    override fun signOut() {
        _auth.signOut()
    }

    override fun isAuthedUser(callback: (Resource<Boolean>) -> Unit) {
        _auth.addAuthStateListener {
            callback(Resource.Success(it.currentUser != null))
        }
    }

    override fun checkUserData(callback: (Resource<Boolean>) -> Unit) {
        val userUid = _auth.currentUser?.uid

        if (userUid != null) {
            database.child("users").child(userUid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").value as? String
                        val surname = snapshot.child("surname").value as? String
                        val phone = snapshot.child("phone").value as? String

                        val isEmpty = name.isNullOrEmpty() || surname.isNullOrEmpty() || phone.isNullOrEmpty()
                        callback(Resource.Success(isEmpty))
                    } else {
                        callback(Resource.Error("User не заполнил данные"))
                    }
                }
                .addOnFailureListener { exception ->
                    callback(Resource.Error(exception.message ?: "Unknown error"))
                }
        } else {
            callback(Resource.Error("User not authenticated"))
        }
    }

    override fun getUserData(callback: (Resource<User?>) -> Unit) {
        val userUid = _auth.currentUser?.uid

        if (userUid != null) {
            database.child("users").child(userUid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        callback(Resource.Success(user))
                    } else {
                        callback(Resource.Error("User does not exist"))
                    }
                }
                .addOnFailureListener { exception ->
                    callback(Resource.Error(exception.message ?: "Unknown error"))
                }
        } else {
            callback(Resource.Error("User not authenticated"))
        }
    } @Suppress("DEPRECATION")

    override suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent, role: String): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = _auth.signInWithCredential(googleCredentials).await().user

            if (user != null) {
                val userUid = user.uid
                val newUser = User(
                    userUid = userUid,
                    username = user.displayName,
                    name = "",
                    surname = "",
                    email = user.email,
                    phone = "",
                    profilePictureUrl = user.photoUrl?.toString(),
                    role = role
                )

                database.child("users").child(userUid).setValue(newUser)
                    .addOnSuccessListener {
                        Log.d("UserRepository", "User data saved successfully")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("UserRepository", "Error writing user data", exception)
                    }
            }

            SignInResult(
                data = user?.run {
                    User(
                        userUid = uid,
                        username = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    override fun checkIsAdmin(callback: (Resource<Boolean>) -> Unit) {
        val userUid = _auth.currentUser?.uid

        if (userUid != null) {
            database.child("users").child(userUid).child("role").get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val role = snapshot.value as? String
                        val isAdmin = role == "Администратор"
                        callback(Resource.Success(isAdmin))
                    } else {
                        callback(Resource.Error("User does not exist"))
                    }
                }
                .addOnFailureListener { exception ->
                    callback(Resource.Error(exception.message ?: "Unknown error"))
                }
        } else {
            callback(Resource.Error("User not authenticated"))
        }
    }

    override fun signInWithOtp(
        otp: String,
        verificationId: String,
        callback: (Resource<SignInResult>) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        _auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    val result = SignInResult(
                        data = user?.run {
                            User(
                                userUid = uid,
                                username = displayName,
                                email = email,
                                profilePictureUrl = photoUrl?.toString()
                            )
                        },
                        errorMessage = null
                    )
                    callback(Resource.Success(result))
                } else {
                    callback(Resource.Error(task.exception?.message ?: "Unknown error"))
                }
            }
    }

    override fun createUserWithPhone(phone: String, callback: (Resource<String>) -> Unit) {
        val activity = context as Activity

        // Паттерн Builder
        val options = PhoneAuthOptions.newBuilder(_auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    _auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(Resource.Success("Verification completed successfully"))
                            } else {
                                callback(Resource.Error(task.exception?.message ?: "Unknown error"))
                            }
                        }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    callback(Resource.Error(e.message ?: "Verification failed"))
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    callback(Resource.Success(verificationId))
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        // Паттерн Builder
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(com.firebase.ui.auth.R.string.default_web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

}