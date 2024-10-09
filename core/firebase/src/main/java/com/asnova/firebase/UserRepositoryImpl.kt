package com.asnova.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
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
    private lateinit var database: DatabaseReference

    private val _auth: FirebaseAuth = Firebase.auth
    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _usersReference: CollectionReference = _database.collection("users")
    private lateinit var onVerificationCode: String

    init {
        database = Firebase.database.reference
    }

    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        database.child("users").child(userId).setValue(user)
    }

    override fun signOut() {
        _auth.signOut()
    }

    override fun isAuthedUser(callback: (Resource<Boolean>) -> Unit) {
        _auth.addAuthStateListener {
            callback(Resource.Success(it.currentUser != null))
        }
    }

    override fun getUserData(callback: (Resource<User?>) -> Unit) {
        val user = _auth.currentUser?.let {
            User(
                userId = it.uid,
                username = it.displayName,
                email = it.email,
                profilePictureUrl = it.photoUrl?.toString()
            )
        }
        callback(Resource.Success(user))
    }

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

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = _auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    User(
                        userId = uid,
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

    override fun pullRequest(callback: (Resource<User?>) -> Unit) {
        callback(Resource.Loading())
        var user: User? = null
        _auth.addAuthStateListener {
            _usersReference.document(it.uid.toString()).get().addOnSuccessListener { snapshot ->
                if (snapshot.data != null) {
                    val currentUser = snapshot.toObject(com.asnova.firebase.model.User::class.java)
                    if (currentUser != null) {
                        user = User(
                            userId = currentUser.userId,
                            profilePictureUrl = currentUser.profilePictureUrl,
                            username = currentUser.username,
                            email = currentUser.email
                        )
                    }
                }
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(Resource.Success(user))
                }
            }.addOnFailureListener { exception ->
                callback(Resource.Error(exception.message.toString()))
            }
        }
    }

    override fun getAllFavorites(callback: (Resource<List<String>>) -> Unit) {
        callback(Resource.Loading())
        pullRequest {
            val user = it.data
            val list: MutableList<String> = mutableListOf()
            if (user != null) {
                _usersReference.document(user.userId).get().addOnSuccessListener { snapshot ->
                    if (snapshot.data != null) {
                        val userData = snapshot.toObject(com.asnova.firebase.model.User::class.java)
//                        userData?.favorites?.forEach { favoritesNewsItem ->
//                            list.add(favoritesNewsItem)
//                        }
                    }
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(Resource.Success(list))
                    }
                }.addOnFailureListener { exception ->
                    callback(Resource.Error(exception.message.toString()))
                }
            }
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
                                userId = uid,
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
}