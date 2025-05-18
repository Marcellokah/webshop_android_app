package com.example.homedecorwebshop;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Custom {@link Application} class for the Home Decor Webshop application.
 * This class is a singleton instance that runs for the entire lifecycle of the application.
 * It is used here to perform initial setup tasks, such as creating notification channels,
 * when the application starts.
 * <p>
 * To use this custom Application class, it must be declared in the {@code AndroidManifest.xml} file
 * within the {@code <application>} tag using the {@code android:name} attribute:
 * <pre>
 * {@code
 * <application
 *     android:name=".MyApplication"
 *     ...>
 *     ...
 * </application>
 * }
 * </pre>
 * </p>
 */
public class MyApplication extends Application {

    /**
     * The unique ID for the "Order Confirmations" notification channel.
     * This ID is used to identify the channel when creating and sending notifications
     * related to order confirmations.
     */
    public static final String ORDER_CONFIRMATION_CHANNEL_ID = "order_confirmation_channel";

    /**
     * Called when the application is starting, before any other application objects have been created.
     * This method is used for global initializations that need to be done once per application lifecycle.
     * In this case, it calls {@link #createNotificationChannels()} to set up notification channels
     * required by the application.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    /**
     * Creates the necessary notification channels for the application.
     * This method checks if the Android version is Oreo (API level 26) or higher,
     * as notification channels were introduced in this version.
     * <p>
     * It creates a high-importance notification channel specifically for "Order Confirmations".
     * Notifications posted to this channel will typically be more intrusive (e.g., make sound, peek).
     * </p>
     * See <a href="https://developer.android.com/develop/ui/views/notifications/channels">Create and manage notification channels</a>
     * for more details.
     */
    private void createNotificationChannels() {
        // Notification channels are only available on Android Oreo (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the "Order Confirmations" channel.
            NotificationChannel orderChannel = new NotificationChannel(
                    ORDER_CONFIRMATION_CHANNEL_ID, // Channel ID
                    "Order Confirmations",         // User-visible name of the channel
                    NotificationManager.IMPORTANCE_HIGH // Importance level
            );
            orderChannel.setDescription("Notifications for confirmed orders"); // User-visible description

            // Get the NotificationManager system service.
            NotificationManager manager = getSystemService(NotificationManager.class);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            if (manager != null) {
                manager.createNotificationChannel(orderChannel);
            }
        }
    }
}