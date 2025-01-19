package com.example.asnova.data

import android.content.SharedPreferences
import android.util.Log
import com.asnova.model.Role
import com.asnova.model.User
import com.asnova.storage.KEY_USER_SETTING

object UserManager {
    private var _role: String = Role.NONE

    var user: User? = null
    var fmc: String = ""

    fun init(sharedPreferences: SharedPreferences) {
        val userRole = sharedPreferences.getString(KEY_USER_SETTING, Role.NONE) ?: Role.NONE
        _role = userRole
        Log.e("login_info", "UserManager role is $_role")
    }

    fun isUserSignedIn(): Boolean {
        return user != null
    }

    fun reset() {
        user = null
        fmc = ""
    }

    fun setRole(newRole: String) {
        _role = newRole
        // TODO() в самом коде сохранение через isAuthedUserStorageImpl.save(Role.ADMIN)
    }

    fun getRole(): String {
        return _role
    }

    fun signOut() {
        _role = Role.NONE
    }
}
