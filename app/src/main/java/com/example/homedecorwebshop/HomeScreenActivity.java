// src/main/java/com/example/homedecorwebshop/HomeScreenActivity.java
package com.example.homedecorwebshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Import FAB
import java.util.ArrayList;
import java.util.List;
import android.view.Menu;    // Import Menu
import android.view.MenuItem; // Import MenuItem
import androidx.annotation.NonNull; // Import NonNull
import androidx.appcompat.widget.Toolbar; // Import Toolbar
import com.google.firebase.auth.FirebaseAuth; // For Logout (if you use Firebase Auth)
// Add other imports as needed for your filter dialog later, like AlertDialog, LayoutInflater, EditText etc.

public class HomeScreenActivity extends AppCompatActivity {

    private LinearLayout itemsContainer;
    private CartManager cartManager;
    private FloatingActionButton fabCart; // Reference to the FAB
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        itemsContainer = findViewById(R.id.itemsContainerLayout);
        cartManager = CartManager.getInstance();
        fabCart = findViewById(R.id.fab_cart);

        if (fabCart != null) {
            fabCart.setOnClickListener(v -> {
                startActivity(new Intent(HomeScreenActivity.this, CartActivity.class));
            });
        }

        List<Item> sampleItems = createSampleItems();
        displayItems(sampleItems);

        updateCartFabVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartFabVisibility(); // Update when returning to this screen
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
        if (itemsContainer == null) return;
        itemsContainer.removeAllViews();

        for (Item item : items) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(0, 0, 0, 24); // Increased padding

            ImageView itemImageView = new ImageView(this);
            itemImageView.setImageResource(item.getImageResourceId());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    200 // Fixed height in pixels (consider using dp)
            );
            imageParams.gravity = Gravity.CENTER_HORIZONTAL;
            imageParams.setMargins(0, 0, 0, 8);
            itemImageView.setLayoutParams(imageParams);
            itemImageView.setAdjustViewBounds(true);
            itemImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // Or CENTER_CROP

            TextView itemTextView = new TextView(this);
            String itemDetails = item.getName() + "\n" +
                    "Price: " + item.getValue() + " HUF\n" +
                    (item.isInStock() ? "In Stock" : "Out of Stock") + "\n" +
                    "Description: " + item.getDescription();
            itemTextView.setText(itemDetails);
            itemTextView.setTextSize(16f);
            itemTextView.setPadding(8,0,8,8);


            Button addToCartButton = new Button(this);
            addToCartButton.setText("Add to Cart");
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            buttonParams.gravity = Gravity.END; // Align button to the right
            buttonParams.setMargins(0, 8, 0, 0);
            addToCartButton.setLayoutParams(buttonParams);

            if (item.isInStock()) {
                addToCartButton.setOnClickListener(v -> {
                    cartManager.addItem(item);
                    Toast.makeText(HomeScreenActivity.this,
                            item.getName() + " has been added to your cart!",
                            Toast.LENGTH_SHORT).show();
                    updateCartFabVisibility(); // Update FAB after adding
                });
            } else {
                addToCartButton.setText("Out of Stock");
                addToCartButton.setEnabled(false);
            }

            itemLayout.addView(itemImageView);
            itemLayout.addView(itemTextView);
            itemLayout.addView(addToCartButton);
            itemsContainer.addView(itemLayout);
        }
    }

    private void updateCartFabVisibility() {
        if (cartManager.getTotalItemCount() > 0) {
            fabCart.setVisibility(View.VISIBLE);
        } else {
            fabCart.setVisibility(View.GONE);
        }
    }

    // --- Menu Inflation and Handling ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu); // Inflate your menu resource file
        return true; // Return true so the menu is displayed
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // The 'if (id == R.id.action_filter)' block has been removed
        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        mAuth.signOut(); // Sign out from Firebase
        Toast.makeText(HomeScreenActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeScreenActivity.this, MainActivity.class); // Navigate to your Login/Main activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish HomeScreenActivity
    }
}