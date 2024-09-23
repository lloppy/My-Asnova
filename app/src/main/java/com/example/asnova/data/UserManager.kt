package com.example.asnova.data

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.asnova.model.Role
import com.asnova.storage.KEY_USER_SETTING

/*
    В Kotlin ключевое слово Object используется для создания объекта-одиночки
    https://apptractor.ru/info/techhype/kotlin-object.html
*/


// Паттерн Singleton
object UserManager {
    private var _role: String = Role.NONE
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

}


/* Второй вариант реализации без использования сахара котлина
    https://www.baeldung.com/kotlin/singleton-classes

class UserManager private constructor() {
    companion object {
        @Volatile
        private var instance: UserManager? = null

        fun getInstance(): UserManager {
            return instance ?: synchronized(this) {
                instance ?: UserManager().also { instance = it }
            }
        }
    }

    private var status: Boolean by mutableStateOf(false)

    fun init(sharedPreferences: SharedPreferences) {
        val userStatus = sharedPreferences.getString(KEY_USER_SETTING, false.toString())
        status = when (userStatus) {
            true.toString() -> true
            false.toString() -> false
            else -> false
        }
        Log.e("login_info", "UserManager status is $userStatus")
    }

    fun getStatus(): Boolean {
        return status
    }

    fun setStatus(newStatus: Boolean) {
        status = newStatus
    }
}
 */