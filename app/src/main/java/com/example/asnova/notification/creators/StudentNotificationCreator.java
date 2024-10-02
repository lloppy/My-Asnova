package com.example.asnova.notification.creators;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.asnova.notification.CoreNotification;
import com.example.asnova.notification.CoreNotificationCreator;
import com.example.asnova.notification.products.StudentMessageNotification;
import com.google.firebase.messaging.RemoteMessage;

// Паттерн Factory method
// конкретный создатель
public class StudentNotificationCreator extends CoreNotificationCreator {

    public StudentNotificationCreator(Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected CoreNotification factoryMethod(String messageType, RemoteMessage remoteMessage) {
        switch (messageType) {
            case StudentMessageNotification.TYPE:
                return new StudentMessageNotification(remoteMessage);
            default:
                return null;
        }
    }
}