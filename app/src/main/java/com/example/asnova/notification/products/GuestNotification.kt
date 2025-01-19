package com.example.asnova.notification.products

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.asnova.screen.sign_in.MainActivity
import com.example.asnova.R
import com.example.asnova.notification.CoreNotification
import com.google.firebase.messaging.RemoteMessage

// Паттерн Factory method
// конкретный продукт
class GuestNotification(remoteMessage: RemoteMessage) : CoreNotification(remoteMessage) {
    override fun configurePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun largeIcon(): Int {
        return R.drawable.ic_guest
    }

    override fun getNotificationTag(): String {
        return "GuestNotification"
    }
}