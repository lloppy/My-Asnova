package com.example.asnova.notification.creators;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.asnova.notification.CoreNotification;
import com.example.asnova.notification.CoreNotificationCreator;
import com.example.asnova.notification.products.MessageNotification;
import com.google.firebase.messaging.RemoteMessage;

// Паттерн Factory method
// конкретный создатель
public class WorkerNotificationCreator extends CoreNotificationCreator {

    public WorkerNotificationCreator(Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected CoreNotification factoryMethod(String messageType, RemoteMessage remoteMessage) {
        switch (messageType) {
            case MessageNotification.TYPE:
                return new MessageNotification(remoteMessage);
            default:
                return null;
        }
    }
}