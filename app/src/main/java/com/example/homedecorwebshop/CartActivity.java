// src/main/java/com/example/homedecorwebshop/CartActivity.java
package com.example.homedecorwebshop;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView tvTotalPrice;
    private TextView tvTotalItems;
    private Button btnClearCart;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart); // Make sure this layout exists

        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Your Cart");
            }
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        cartItemsContainer = findViewById(R.id.cartItemsContainerLayout);
        tvTotalPrice = findViewById(R.id.tvCartTotalPrice);
        tvTotalItems = findViewById(R.id.tvCartTotalItems);
        btnClearCart = findViewById(R.id.btnClearCart);

        cartManager = CartManager.getInstance();

        if (btnClearCart != null) {
            btnClearCart.setOnClickListener(v -> {
                cartManager.clearCart();
                displayCartItems(); // Refresh display
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
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f); // Weight for text
                itemNameAndQuantity.setLayoutParams(textParams);

                TextView itemPrice = new TextView(this);
                itemPrice.setText(String.format(Locale.getDefault(), "%,d HUF", item.getValue() * quantity));
                itemPrice.setTextSize(16f);
                itemPrice.setGravity(Gravity.END);
                LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                itemPrice.setLayoutParams(priceParams);

                // Optional: Add buttons to increase/decrease quantity or remove item
                Button btnRemoveOne = new Button(this);
                btnRemoveOne.setText("-");
                btnRemoveOne.setOnClickListener(v -> {
                    cartManager.removeItem(item);
                    displayCartItems(); // Refresh the cart display
                });

                // Add more buttons or functionality as needed (e.g. remove all of one item)

                itemRowLayout.addView(itemNameAndQuantity);
                // Add +/- buttons before the price if you like
                itemRowLayout.addView(btnRemoveOne); // Example of remove one button
                itemRowLayout.addView(itemPrice);

                cartItemsContainer.addView(itemRowLayout);
            }
        }
        // Update totals
        tvTotalItems.setText(String.format(Locale.getDefault(),"Total Items: %d", cartManager.getTotalItemCount()));
        tvTotalPrice.setText(String.format(Locale.getDefault(),"Total Price: %,.0f HUF", cartManager.getTotalPrice()));
    }
}