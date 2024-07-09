package com.example.asnova.utils.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigate(route: String, params: Bundle?, builder: NavOptionsBuilder.() -> Unit = {}) {
    this.currentBackStackEntry?.arguments?.putAll(params)

    navigate(route, builder)
}