package com.example.homedecorwebshop;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView tvTotalPrice;
    private TextView tvTotalItems;
    private CartManager cartManager;
    private static final String TAG = "CartActivityNotification";
    private static final int ORDER_CONFIRM_NOTIFICATION_ID = 102;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d(TAG, "Notification permission granted via launcher.");
                    sendOrderConfirmedNotification();
                } else {
                    Log.w(TAG, "Notification permission denied via launcher.");
                    Toast.makeText(this, "Notification permission denied. Cannot show order confirmation.", Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Cart");
            }
        }

        Button btnCheckout = findViewById(R.id.btnProceedToCheckout);

        btnCheckout.setOnClickListener(v -> {
            Log.d(TAG, "Checkout button clicked.");
            attemptToSendSimpleNotification();
        });

        cartItemsContainer = findViewById(R.id.cartItemsContainerLayout);
        tvTotalPrice = findViewById(R.id.tvCartTotalPrice);
        tvTotalItems = findViewById(R.id.tvCartTotalItems);
        Button btnClearCart = findViewById(R.id.btnClearCart);

        cartManager = CartManager.getInstance();

        if (btnClearCart != null) {
            btnClearCart.setOnClickListener(v -> {
                cartManager.clearCart();
                displayCartItems();
            });
        }
        displayCartItems();
    }

    private void displayCartItems() {
        cartItemsContainer.removeAllViews();
        Map<Item, Integer> itemsWithQuantities = cartManager.getCartItems();

        if (itemsWithQuantities.isEmpty()) {
            TextView emptyCartTextView = new TextView(this);
            emptyCartTextView.setText("Your cart is empty.");
            emptyCartTextView.setTextSize(18f);
            emptyCartTextView.setPadding(16, 16, 16, 16);
            emptyCartTextView.setGravity(Gravity.CENTER);
            cartItemsContainer.addView(emptyCartTextView);
        } else {
            for (Map.Entry<Item, Integer> entry : itemsWithQuantities.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();

                LinearLayout itemRowLayout = new LinearLayout(this);
                itemRowLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemRowLayout.setPadding(8, 16, 8, 16);
                itemRowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                TextView itemNameAndQuantity = new TextView(this);
                itemNameAndQuantity.setText(String.format(Locale.getDefault(), "%s (Qty: %d)", item.getName(), quantity));
                itemNameAndQuantity.setTextSize(16f);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                itemNameAndQuantity.setLayoutParams(textParams);

                TextView itemPrice = new TextView(this);
                itemPrice.setText(String.format(Locale.getDefault(), "%,d HUF", item.getValue() * quantity));
                itemPrice.setTextSize(16f);
                itemPrice.setGravity(Gravity.END);
                LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                itemPrice.setLayoutParams(priceParams);

                Button btnRemoveOne = new Button(this);
                btnRemoveOne.setText("-");
                btnRemoveOne.setOnClickListener(v -> {
                    cartManager.removeItem(item);
                    displayCartItems();
                });

                itemRowLayout.addView(itemNameAndQuantity);
                itemRowLayout.addView(btnRemoveOne);
                itemRowLayout.addView(itemPrice);

                cartItemsContainer.addView(itemRowLayout);
            }
        }
        tvTotalItems.setText(String.format(Locale.getDefault(), "Total Items: %d", cartManager.getTotalItemCount()));
        tvTotalPrice.setText(String.format(Locale.getDefault(), "Total Price: %,.0f HUF", cartManager.getTotalPrice()));
    }

    private void attemptToSendSimpleNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission already granted.");
                sendOrderConfirmedNotification();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.d(TAG, "Showing notification permission rationale (though keeping it simple).");
                Toast.makeText(this, "Notification permission is needed to show your order confirmation.", Toast.LENGTH_LONG).show();
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                Log.d(TAG, "Requesting notification permission.");
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            Log.d(TAG, "Pre-Android 13, proceeding to send notification.");
            sendOrderConfirmedNotification();
        }
    }

    private void sendOrderConfirmedNotification() {
        Log.d(TAG, "Preparing to send order confirmed notification.");

        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApplication.ORDER_CONFIRMATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_order_confirmed)
                .setContentTitle("Order Confirmed!")
                .setContentText("Your order has been confirmed!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.w(TAG, "Permission check failed just before sending. User likely denied via dialog.");
            return;
        }

        try {
            notificationManager.notify(ORDER_CONFIRM_NOTIFICATION_ID, builder.build());
            Log.d(TAG, "Order confirmed notification sent successfully.");
            Toast.makeText(this, "Order Confirmed! Notification Sent.", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException while sending notification. This usually means permission is missing or revoked.", e);
            Toast.makeText(this, "Could not send notification due to a security setting.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}