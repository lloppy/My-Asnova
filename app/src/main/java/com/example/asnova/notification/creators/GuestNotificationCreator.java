package com.example.asnova.notification.creators;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.asnova.notification.CoreNotification;
import com.example.asnova.notification.CoreNotificationCreator;
import com.example.asnova.notification.products.GuestNotification;
import com.google.firebase.messaging.RemoteMessage;

// Паттерн Factory method
// конкретный создатель
public class GuestNotificationCreator extends CoreNotificationCreator {

    public GuestNotificationCreator(Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected CoreNotification factoryMethod(String messageType, RemoteMessage remoteMessage) {
        switch (messageType) {
            case "guest_message":
                return new GuestNotification(remoteMessage);
            default:
                return null;
        }
    }
}