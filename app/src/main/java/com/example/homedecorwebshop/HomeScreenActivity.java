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

/**
 * HomeScreenActivity serves as the main screen of the application, displaying a list of items
 * available for purchase. Users can add items to their cart and navigate to the cart screen.
 * It also provides a logout functionality through an options menu.
 * <p>
 * The activity uses a {@link LinearLayout} to dynamically inflate and display item views.
 * Item data is currently sourced from a sample list created within the activity.
 * It interacts with {@link CartManager} to manage cart operations and updates a
 * {@link FloatingActionButton} to reflect the cart's state. Firebase Authentication is used
 * for user logout.
 * </p>
 */
public class HomeScreenActivity extends AppCompatActivity {

    private LinearLayout itemsContainer;
    private CartManager cartManager;
    private FloatingActionButton fabCart;
    private FirebaseAuth mAuth;

    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up: create views, bind data to lists, etc. This method also initializes
     * the toolbar, Firebase Authentication, CartManager, and the Floating Action Button for the cart.
     * It then loads and displays a list of sample items.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
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
            // Animate the FAB on creation
            fabCart.setScaleX(0f);
            fabCart.setScaleY(0f);
            fabCart.animate().scaleX(1f).scaleY(1f).setDuration(300).setStartDelay(500).setInterpolator(new android.view.animation.OvershootInterpolator()).start();
            fabCart.setOnClickListener(v -> startActivity(new Intent(HomeScreenActivity.this, CartActivity.class)));
        }

        List<Item> sampleItems = createSampleItems();
        displayItems(sampleItems);

        updateCartFabVisibility();
    }

    /**
     * Called when the activity will start interacting with the user. At this point your activity
     * is at the top of the activity stack, with user input going to it.
     * This is a good place to update the visibility of the cart FAB, as the cart contents
     * might have changed while another activity was in the foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateCartFabVisibility();
    }

    /**
     * Creates and returns a predefined list of sample {@link Item} objects.
     * This method is used for populating the home screen with initial data.
     * In a production app, this data would typically come from a database or a network API.
     *
     * @return A {@link List} of sample {@code Item} objects.
     */
    private List<Item> createSampleItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Modern Vase", 12000, true, R.drawable.modern_vase, "A sleek and stylish modern vase."));
        items.add(new Item("Rustic Wooden Lamp", 25000, true, R.drawable.wooden_lamp, "Handcrafted rustic lamp with a warm glow."));
        items.add(new Item("Bohemian Wall Tapestry", 8500, false, R.drawable.bohemian_wall_tapestry, "Colorful tapestry to add a bohemian touch."));
        items.add(new Item("Minimalist Desk Organizer", 6000, true, R.drawable.minimalist_desk_organizer, "Keep your desk tidy with this minimalist organizer."));
        return items;
    }

    /**
     * Dynamically inflates views for each {@link Item} in the provided list and adds them
     * to the {@code itemsContainer} {@link LinearLayout}.
     * Each item view is inflated from {@code R.layout.item_layout} and populated with the
     * item's details (name, price, description, image, stock status).
     * It also sets up "Add to Cart" and "View Details" button interactions.
     * An animation is applied to each item view as it's added.
     *
     * @param items A {@link List} of {@code Item} objects to be displayed.
     *              If the list is null or empty, or if {@code itemsContainer} is null,
     *              the method will not display any items.
     */
    private void displayItems(List<Item> items) {
        Log.d("HomeScreenActivity", "Entering displayItems");
        if (itemsContainer == null) {
            Log.e("HomeScreenActivity", "displayItems: itemsContainer is null!");
            Toast.makeText(this, "Error displaying items. Container not found.", Toast.LENGTH_LONG).show();
            return;
        }
        itemsContainer.removeAllViews(); // Clear previous items

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
                    continue; // Skip this item if inflation fails
                }
            } catch (Exception e) {
                Log.e("HomeScreenActivity", "Error inflating item_layout for " + item.getName(), e);
                continue; // Skip this item if inflation throws an exception
            }

            // Find views within the inflated item layout
            ImageView itemImageView = itemView.findViewById(R.id.itemImageView);
            TextView itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            TextView itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            TextView itemStockTextView = itemView.findViewById(R.id.itemStockTextView);
            TextView itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            MaterialButton btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            MaterialButton btnViewDetails = itemView.findViewById(R.id.btnViewDetails); // Optional "View Details" button

            // Basic null check for critical views
            if (itemImageView == null || itemNameTextView == null || itemPriceTextView == null || itemStockTextView == null || itemDescriptionTextView == null || btnAddToCart == null) {
                continue;
            }

            // Set the item's name to the corresponding TextView.
            itemNameTextView.setText(item.getName());

            // Format the item's price with the local currency (HUF) and set it to the TextView.
            String formattedPrice = String.format(Locale.getDefault(), "%,d HUF", item.getValue());
            itemPriceTextView.setText(formattedPrice);

            // Set the item's description to the corresponding TextView.
            itemDescriptionTextView.setText(item.getDescription());

            // Set the item's image to the corresponding ImageView.
            itemImageView.setImageResource(item.getImageResourceId());

            // Check the stock status of the item and update UI elements accordingly.
            if (item.isInStock()) {
                // If in stock, display "In Stock", set text color, enable "Add to Cart" button.
                itemStockTextView.setText(getString(R.string.in_stock));
                itemStockTextView.setTextColor(ContextCompat.getColor(this, R.color.olive_secondary));
                btnAddToCart.setText(getString(R.string.add_to_cart));
                btnAddToCart.setEnabled(true);
                // Set an OnClickListener for the "Add to Cart" button.
                // Adds the item to the cart, shows a confirmation Toast, and updates the cart FAB visibility.
                btnAddToCart.setOnClickListener(v -> {
                    cartManager.addItem(item);
                    Toast.makeText(HomeScreenActivity.this, item.getName() + " " + getString(R.string.added_to_cart_toast), Toast.LENGTH_SHORT).show();
                    updateCartFabVisibility();
                });
            } else {
                // If out of stock, display "Out of Stock", set text color, and disable "Add to Cart" button.
                itemStockTextView.setText(getString(R.string.out_of_stock));
                itemStockTextView.setTextColor(ContextCompat.getColor(this, R.color.soft_red_error));
                btnAddToCart.setText(getString(R.string.out_of_stock_button));
                btnAddToCart.setEnabled(false);
            }

            // If the "View Details" button exists, set its OnClickListener.
            // Shows a Toast indicating which item's details are being viewed (placeholder functionality).
            if (btnViewDetails != null) {
                btnViewDetails.setOnClickListener(v -> Toast.makeText(HomeScreenActivity.this, getString(R.string.viewing_details_for) + " " + item.getName(), Toast.LENGTH_SHORT).show());
            }

            // Add the fully populated item view to the main container.
            itemsContainer.addView(itemView);
            Log.d("HomeScreenActivity", "Added itemView for: " + item.getName());

            // Try to load and apply an animation to the item view as it's added.
            try {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_slide_in_from_bottom);
                itemView.startAnimation(animation);
            } catch (Exception e) {
                // Log an error if the animation fails to load or apply.
                Log.e("HomeScreenActivity", "Error applying animation for " + item.getName(), e);
            }
        }
        Log.d("HomeScreenActivity", "Exiting displayItems");
    }

    /**
     * Updates the visibility of the Floating Action Button (FAB) for the cart.
     * If there are items in the cart (total item count > 0), the FAB is made visible.
     * Otherwise, the FAB is hidden (gone).
     */
    private void updateCartFabVisibility() {
        if (cartManager.getTotalItemCount() > 0) {
            fabCart.setVisibility(View.VISIBLE);
        } else {
            fabCart.setVisibility(View.GONE);
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu. You should
     * place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed. To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to allow normal menu processing to
     * proceed, calling the item's Runnable or sending a message to its Handler as appropriate.
     * <p>
     * This method handles the "Logout" action.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle the "Logout" action.
        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logs out the current user using Firebase Authentication.
     * It signs the user out, displays a "Logged out" Toast message,
     * navigates the user back to the {@link MainActivity} (presumably the login screen),
     * and clears the activity task stack to prevent returning to the home screen via the back button.
     * Finally, it finishes the current {@code HomeScreenActivity}.
     */
    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(HomeScreenActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeScreenActivity.this, MainActivity.class);
        // Clear the activity stack and start a new task for MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish HomeScreenActivity
    }
}