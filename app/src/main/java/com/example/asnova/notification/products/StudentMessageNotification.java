package com.example.asnova.notification.products;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.asnova.R;
import com.example.asnova.notification.CoreNotification;
import com.example.asnova.screen.settings.components.MessageActivity;
import com.google.firebase.messaging.RemoteMessage;

// Паттерн Factory method
// конкретный продукт
public class StudentMessageNotification extends CoreNotification {
    public static final String TYPE = "student_message";

    public StudentMessageNotification(RemoteMessage remoteMessage) {
        super(remoteMessage);
    }

    @Override
    protected PendingIntent configurePendingIntent(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected int largeIcon() {
        return R.drawable.ic_student_message;
    }

    @Override
    protected String getNotificationTag() {
        return getClass().getName();
    }
}