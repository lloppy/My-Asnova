package com.asnova.firebase.export

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

// Паттерн Composite
class DatabaseExporter {
    private var _database: DatabaseReference = Firebase.database.reference

    fun loadDatabase() {
        val root = RootNode()

        _database.child("asnovaAppAdmins").get().addOnSuccessListener { adminSnapshot ->
            if (adminSnapshot.exists()) {
                adminSnapshot.children.forEach { admin ->
                    val adminId = admin.key ?: ""
                    val email = admin.child("email").value as? String ?: ""
                    root.addChild(AdminNode(adminId, email))
                }
            } else {
                Log.w("DatabaseExporter", "No admins found")
            }

            _database.child("asnovaClasses").get().addOnSuccessListener { classSnapshot ->
                if (classSnapshot.exists()) {
                    classSnapshot.children.forEach { classItem ->
                        val classId = classItem.key ?: ""
                        val name = classItem.child("name").value as? String ?: ""
                        root.addChild(ClassNode(classId, name))
                    }
                } else {
                    Log.w("DatabaseExporter", "No classes found")
                }

                _database.child("users").get().addOnSuccessListener { userSnapshot ->
                    if (userSnapshot.exists()) {
                        userSnapshot.children.forEach { user ->
                            val userId = user.key ?: ""
                            val username = user.child("username").value as? String ?: ""
                            val email = user.child("email").value as? String ?: ""
                            root.addChild(UserNode(userId, username, email))
                        }
                    } else {
                        Log.w("DatabaseExporter", "No users found")
                    }
                }
            }
        }

        Log.i("DatabaseExporter", "All data: ${root.getData()}")
    }
}