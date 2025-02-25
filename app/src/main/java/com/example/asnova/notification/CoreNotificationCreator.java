package com.example.asnova.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.asnova.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

// Паттерн Factory method
// создатель абстрактный
public abstract class CoreNotificationCreator {

    private static final String KEY_NOTIFICATION_TAG = "CoreNotificationCreator.TagKey";
    private static final String DEFAULT_TAG = "CoreNotificationCreator.DefaultTag";

    private static final String KEY_TYPE = "type";

    private NotificationManager notificationManager;

    public CoreNotificationCreator(Context context) {
        notificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
    }

    public void showNotification(Context context, RemoteMessage remoteMessage) {
        String notificationType = getNotificationType(remoteMessage);
        CoreNotification notification = factoryMethod(notificationType, remoteMessage);
        if (notification != null) {
            NotificationCompat.Builder builder = builderFromPushNotification(context, notification);
            notify(builder);
        }
    }

    private String getNotificationType(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        if (data.containsKey(KEY_TYPE)) {
            return data.get(KEY_TYPE);
        }
        return "";
    }

    @Nullable
    // Паттерн Factory method
    // сам метод в абстрактном создателе
    protected abstract CoreNotification factoryMethod(String messageType, RemoteMessage remoteMessage);

    private final static int DEFAULT_NOTIFICATION_ID = 15;

    private static final
    @DrawableRes
    int SMALL_ICON_RES_ID = R.drawable.ic_asnova_default_news;

    protected NotificationCompat.Builder builderFromPushNotification(Context context, CoreNotification notification) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), notification.largeIcon());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(SMALL_ICON_RES_ID)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentTitle(notification.getTitle())
            .setContentText(notification.getContent())
            .setLargeIcon(largeIcon);
        builder.getExtras().putString(KEY_NOTIFICATION_TAG, notification.getNotificationTag());
        builder.setContentIntent(notification.configurePendingIntent(context));
        return builder;
    }

    // Паттерн Builder
    private void notify(@NonNull NotificationCompat.Builder builder) {
        final String notificationTag = getNotificationTag(builder);
        notificationManager.cancel(notificationTag, DEFAULT_NOTIFICATION_ID);
        notificationManager.notify(notificationTag, DEFAULT_NOTIFICATION_ID, builder.build());
    }

    private String getNotificationTag(NotificationCompat.Builder builder) {
        Bundle extras = builder.getExtras();
        if (extras.containsKey(KEY_NOTIFICATION_TAG)) {
            return extras.getString(KEY_NOTIFICATION_TAG);
        } else {
            return DEFAULT_TAG;
        }
    }
}