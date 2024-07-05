package com.example.asnova.utils

import android.content.Context
import android.widget.Toast

const val LOG_IN = "LOG_IN LOG"

fun toastMessage(context: Context, message:String){
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()
}