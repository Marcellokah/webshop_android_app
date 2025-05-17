package com.example.homedecorwebshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends AppCompatActivity {

    private LinearLayout itemsContainer;
    private CartManager cartManager;
    private FloatingActionButton fabCart;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.d("HomeScreenActivity", "onCreate started");

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        Log.d("HomeScreenActivity", "Toolbar set");

        mAuth = FirebaseAuth.getInstance();

        itemsContainer = findViewById(R.id.itemsContainerLayout);
        if (itemsContainer == null) {
            Log.e("HomeScreenActivity", "itemsContainer is NULL!");
        } else {
            Log.d("HomeScreenActivity", "itemsContainer found");
        }

        cartManager = CartManager.getInstance();

        fabCart = findViewById(R.id.fab_cart);
        if (fabCart == null) {
            Log.e("HomeScreenActivity", "fabCart is NULL!");
        } else {
            Log.d("HomeScreenActivity", "fabCart found");
            fabCart.setScaleX(0f);
            fabCart.setScaleY(0f);
            fabCart.animate().scaleX(1f).scaleY(1f).setDuration(300).setStartDelay(500).setInterpolator(new android.view.animation.OvershootInterpolator()).start();
            fabCart.setOnClickListener(v -> startActivity(new Intent(HomeScreenActivity.this, CartActivity.class)));
        }

        List<Item> sampleItems = createSampleItems();
        displayItems(sampleItems);

        updateCartFabVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartFabVisibility();
    }

    private List<Item> createSampleItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Modern Vase", 12000, true, R.drawable.modern_vase, "A sleek and stylish modern vase."));
        items.add(new Item("Rustic Wooden Lamp", 25000, true, R.drawable.wooden_lamp, "Handcrafted rustic lamp with a warm glow."));
        items.add(new Item("Bohemian Wall Tapestry", 8500, false, R.drawable.bohemian_wall_tapestry, "Colorful tapestry to add a bohemian touch."));
        items.add(new Item("Minimalist Desk Organizer", 6000, true, R.drawable.minimalist_desk_organizer, "Keep your desk tidy with this minimalist organizer."));
        return items;
    }

    private void displayItems(List<Item> items) {
        Log.d("HomeScreenActivity", "Entering displayItems");
        if (itemsContainer == null) {
            Log.e("HomeScreenActivity", "displayItems: itemsContainer is null!");
            Toast.makeText(this, "Error displaying items. Container not found.", Toast.LENGTH_LONG).show();
            return;
        }
        itemsContainer.removeAllViews();

        if (items == null || items.isEmpty()) {
            Log.d("HomeScreenActivity", "displayItems: No items to display.");
            return;
        }
        Log.d("HomeScreenActivity", "displayItems: Number of items: " + items.size());

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            Log.d("HomeScreenActivity", "Processing item: " + item.getName());

            View itemView;
            try {
                itemView = inflater.inflate(R.layout.item_layout, itemsContainer, false);
                if (itemView == null) {
                    Log.e("HomeScreenActivity", "itemView is NULL after inflation for item: " + item.getName());
                    continue;
                }
            } catch (Exception e) {
                Log.e("HomeScreenActivity", "Error inflating item_layout for " + item.getName(), e);
                continue;
            }

            ImageView itemImageView = itemView.findViewById(R.id.itemImageView);
            TextView itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            TextView itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            TextView itemStockTextView = itemView.findViewById(R.id.itemStockTextView);
            TextView itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            MaterialButton btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            MaterialButton btnViewDetails = itemView.findViewById(R.id.btnViewDetails);

            if (itemImageView == null || itemNameTextView == null || itemPriceTextView == null || itemStockTextView == null || itemDescriptionTextView == null || btnAddToCart == null) {
                Log.e("HomeScreenActivity", "One or more views in item_layout are null for item: " + item.getName());
                continue;
            }

            itemNameTextView.setText(item.getName());

            String formattedPrice = String.format(Locale.getDefault(), "%,d HUF", item.getValue());
            itemPriceTextView.setText(formattedPrice);

            itemDescriptionTextView.setText(item.getDescription());

            itemImageView.setImageResource(item.getImageResourceId());

            if (item.isInStock()) {
                itemStockTextView.setText(getString(R.string.in_stock));
                itemStockTextView.setTextColor(ContextCompat.getColor(this, R.color.olive_secondary));
                btnAddToCart.setText(getString(R.string.add_to_cart));
                btnAddToCart.setEnabled(true);
                btnAddToCart.setOnClickListener(v -> {
                    cartManager.addItem(item);
                    Toast.makeText(HomeScreenActivity.this, item.getName() + " " + getString(R.string.added_to_cart_toast), Toast.LENGTH_SHORT).show();
                    updateCartFabVisibility();
                });
            } else {
                itemStockTextView.setText(getString(R.string.out_of_stock));
                itemStockTextView.setTextColor(ContextCompat.getColor(this, R.color.soft_red_error));
                btnAddToCart.setText(getString(R.string.out_of_stock_button));
                btnAddToCart.setEnabled(false);
            }

            if (btnViewDetails != null) {
                btnViewDetails.setOnClickListener(v -> Toast.makeText(HomeScreenActivity.this, getString(R.string.viewing_details_for) + " " + item.getName(), Toast.LENGTH_SHORT).show());
            }

            itemsContainer.addView(itemView);
            Log.d("HomeScreenActivity", "Added itemView for: " + item.getName());

            try {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_slide_in_from_bottom);
                itemView.startAnimation(animation);
            } catch (Exception e) {
                Log.e("HomeScreenActivity", "Error applying animation for " + item.getName(), e);
            }
        }
        Log.d("HomeScreenActivity", "Exiting displayItems");
    }

    private void updateCartFabVisibility() {
        if (cartManager.getTotalItemCount() > 0) {
            fabCart.setVisibility(View.VISIBLE);
        } else {
            fabCart.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(HomeScreenActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeScreenActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}