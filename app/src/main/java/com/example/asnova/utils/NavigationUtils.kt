package com.example.asnova.utils

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigate(
    route: String,
    params: Bundle?,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    this.currentBackStackEntry?.arguments?.putAll(params)
    navigate(route, builder)
}

fun createExternalRouter(block: (String, Bundle) -> Unit): Router = object : Router {
    override fun routeTo(screen: String, params: Bundle) {
        block.invoke(screen, params)
    }
}

interface Router {
    fun routeTo(screen: String, params: Bundle = bundleOf()) {
        throw NotImplementedError(message = "You used router, but don't implemented it for screen $screen with params $params")
    }
}