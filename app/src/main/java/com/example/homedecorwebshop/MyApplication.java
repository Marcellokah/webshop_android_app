package com.example.homedecorwebshop; // Use your app's package name

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {

    public static final String ORDER_CONFIRMATION_CHANNEL_ID = "order_confirmation_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel orderChannel = new NotificationChannel(
                    ORDER_CONFIRMATION_CHANNEL_ID,
                    "Order Confirmations",
                    NotificationManager.IMPORTANCE_HIGH
            );
            orderChannel.setDescription("Notifications for confirmed orders");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(orderChannel);
            }
        }
    }
}