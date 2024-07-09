package com.asnova.firebase

import com.asnova.domain.repository.firebase.UserRepository
import com.asnova.model.Resource
import com.asnova.model.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val _auth: FirebaseAuth = Firebase.auth
    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _databaseReference: CollectionReference = _database.collection("users")

    override fun createUserWithEmailAndPassword(
        username: String,
        email: String,
        password: String,
        callback: (Resource<User?>) -> Unit
    ) {
        _auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            _auth.currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { sendEmailVerificationTask ->
                    if (sendEmailVerificationTask.isSuccessful) {
                        val currentUser = it.user
                        if (currentUser != null) {
                            val metadata = currentUser.metadata
                            if (metadata != null) {
                                val user = com.asnova.firebase.model.User(
                                    uid = currentUser.uid,
                                    isAnonymous = currentUser.isAnonymous,
                                    photoUrl = currentUser.photoUrl.toString(),
                                    displayName = username,
                                    email = currentUser.email.toString(),
                                    isEmailVerified = currentUser.isEmailVerified,
                                    phoneNumber = currentUser.phoneNumber.toString(),
                                    creationTimestamp = Timestamp(Date(metadata.creationTimestamp)),
                                    lastSignInTimestamp = Timestamp(Date(metadata.lastSignInTimestamp)),
                                    favorites = emptyList()
                                )
                                _databaseReference.document(currentUser.uid).set(user)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            pullRequest(callback = { user ->
                                                callback(Resource.Success(user.data))
                                            })
                                        }
                                    }
                            }
                        }
                    }
                }
        }.addOnCompleteListener {
        }.addOnFailureListener {
        }
    }

    override fun signInUserWithEmailAndPassword(
        email: String,
        password: String,
        callback: (Resource<User?>) -> Unit
    ) {
        _auth.signInWithEmailAndPassword(email, password).addOnSuccessListener { task ->
            val user = task.user
            if (user != null) {
                pullRequest(callback = {
                    callback(it)
                })
            }
        }.addOnFailureListener {
        }
    }

    override fun signOutUser() {
        _auth.signOut()
    }

    override fun isAuthedUser(callback: (Resource<Boolean>) -> Unit) {
        _auth.addAuthStateListener {
            callback(Resource.Success(it.currentUser != null))
        }
    }

    override fun pullRequest(callback: (Resource<User?>) -> Unit) {
        callback(Resource.Loading())
        var user: User? = null
        _auth.addAuthStateListener {
            _databaseReference.document(it.uid.toString()).get().addOnSuccessListener { snapshot ->
                if (snapshot.data != null) {
                    val currentUser = snapshot.toObject(com.asnova.firebase.model.User::class.java)
                    if (currentUser != null) {
                        user = User(
                            uid = currentUser.uid,
                            isAnonymous = currentUser.isAnonymous,
                            photoUrl = currentUser.photoUrl,
                            displayName = currentUser.displayName,
                            email = currentUser.email,
                            isEmailVerified = currentUser.isEmailVerified,
                            phoneNumber = currentUser.phoneNumber,
                            creationTimestamp = currentUser.creationTimestamp.seconds,
                            lastSignInTimestamp = currentUser.lastSignInTimestamp.seconds,
                            favorites = emptyList()
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

    override fun addNewsItemToFavorites(id: String, callback: (Resource<Boolean>) -> Unit) {
        pullRequest {
            val user = it.data
            if (user != null) {
                _databaseReference.document(user.uid).update("favorites", FieldValue.arrayUnion(id))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                            callback(Resource.Success(true))
                        if (task.isCanceled)
                            callback(Resource.Success(false))
                    }
            }
        }
    }

    override fun getAllFavorites(callback: (Resource<List<String>>) -> Unit) {
        callback(Resource.Loading())
        pullRequest {
            val user = it.data
            val list: MutableList<String> = mutableListOf()
            if (user != null) {
                _databaseReference.document(user.uid).get().addOnSuccessListener { snapshot ->
                    if (snapshot.data != null) {
                        val userData = snapshot.toObject(com.asnova.firebase.model.User::class.java)
                        userData?.favorites?.forEach { favoritesNewsItem ->
                            list.add(favoritesNewsItem)
                        }
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
}