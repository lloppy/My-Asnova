package com.example.asnova.data

import android.content.SharedPreferences
import android.util.Log
import com.asnova.model.Role
import com.asnova.storage.KEY_USER_SETTING

/*
    В Kotlin ключевое слово Object используется для создания объекта-одиночки
    https://apptractor.ru/info/techhype/kotlin-object.html
*/

// Паттерн Singleton
object UserManager {
    private var _role: String = Role.NONE
    var status = _role == Role.ADMIN
    var fmc = ""
    fun init(sharedPreferences: SharedPreferences) {
        val userRole = sharedPreferences.getString(KEY_USER_SETTING, Role.NONE) ?: Role.NONE
        _role = userRole
        Log.e("login_info", "UserManager role is $_role")
    }

    fun setRole(newRole: String) {
        _role = newRole
        // TODO() в самом коде сохранение через юзкейс
        // sharedPreferences.edit().putString(KEY_USER_SETTING, newRole).apply()
    }

    fun getRole(): String {
        return _role
    }

    fun signOut() {
        _role = Role.NONE
    }
}
