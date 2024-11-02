package com.asnova.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Promocode
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
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient
) : UserRepository {
    private var _database: DatabaseReference = Firebase.database.reference
    private val _auth: FirebaseAuth = Firebase.auth
    private val adminsRef = _database.child("asnovaAppAdmins")
    private val promoRef = _database.child("asnovaPromocode")

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

            _database.child("users").child(userUid).setValue(user)
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
            _database.child("users").child(userUid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").value as? String
                        val surname = snapshot.child("surname").value as? String
                        val phone = snapshot.child("phone").value as? String

                        val isEmpty =
                            name.isNullOrEmpty() || surname.isNullOrEmpty() || phone.isNullOrEmpty()
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

    override fun checkUserClass(callback: (Resource<Boolean>) -> Unit) {
        val userUid = _auth.currentUser?.uid

        if (userUid != null) {
            _database.child("users").child(userUid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val asnovaClass = snapshot.child("asnovaClass").value as? String
                        callback(Resource.Success(asnovaClass.isNullOrEmpty()))
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
            _database.child("users").child(userUid).get()
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
    }

    override suspend fun signInWithLauncher(): IntentSender? {
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

    override fun signInWithIntent(
        intent: Intent,
        role: String,
        fmc: String,
        callback: (Resource<SignInResult>) -> Unit
    ) {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        _auth.signInWithCredential(googleCredentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user

                val result = SignInResult(
                    data = user?.run {
                        User(
                            userUid = user.uid,
                            username = user.displayName,
                            name = "",
                            surname = "",
                            email = user.email,
                            phone = "",
                            fmc = fmc,
                            asnovaClass = "",
                            profilePictureUrl = user.photoUrl?.toString(),
                            role = role
                        )
                    },
                    errorMessage = null
                )

                result.data?.userUid?.let { userUid ->
                    _database.child("users").child(userUid).setValue(result.data)
                        .addOnSuccessListener {
                            Log.d("UserRepository", "User data saved successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("UserRepository", "Error writing user data", exception)
                        }
                } ?: run {
                    Log.e("UserRepository", "User data is null, cannot save to database")
                }

                callback(Resource.Success(result))
            } else {
                callback(Resource.Error(task.exception?.message ?: "Unknown error"))
            }
        }
    }


    override fun checkIsAdmin(callback: (Resource<Boolean>) -> Unit) {
        var isAdmin = false
        val userUid = _auth.currentUser?.uid

        if (userUid != null) {

            _database.child("users").child(userUid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val role = snapshot.child("role").value as? String
                        val email = snapshot.child("email").value as? String

                        isAdmin = role == "Администратор"

                        if (!isAdmin) {
                            adminsRef.get().addOnSuccessListener { admins ->

                                admins.children.forEach { doc ->
                                    val isAdmin2 =
                                        doc.child("isAdmin").getValue(Boolean::class.java)
                                    val email2 = doc.child("email").getValue(String::class.java)

                                    if (isAdmin2 != null && email2 != null) {
                                        if (isAdmin2 && email2 == email) {
                                            isAdmin = isAdmin2
                                            _database.child("users").child(userUid).child("role")
                                                .setValue("Администратор")
                                        }
                                    }
                                }

                            }
                        }
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

    override fun submitPromocode(
        promocode: String,
        userData: User,
        callback: (Resource<String>) -> Unit
    ) {
        promoRef.get().addOnSuccessListener {
            val promoKey = promoRef.push().key

            if (promoKey != null) {
                val promocodeData = Promocode(promocode, user = userData, approved = false)
                promoRef.child(promoKey).setValue(promocodeData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("PromoCode", "Промокод успешно сохранен с ключом: $promoKey")
                        callback(Resource.Success(promocode))
                    } else {
                        callback(Resource.Error("Failed to set promocode"))
                        Log.e(
                            "PromoCode",
                            "Ошибка при сохранении промокода: ${task.exception?.message}"
                        )
                    }
                }
            } else {
                callback(Resource.Error("Failed to set promocode"))
                Log.e("PromoCode", "Не удалось сгенерировать уникальный ключ для промокода.")
            }

        }.addOnFailureListener { exception ->
            callback(Resource.Error(exception.message ?: "Unknown error"))
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

    override fun sendOtp(phone: String, callback: (Resource<String>) -> Unit) {
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