// src/main/java/com/example/homedecorwebshop/HomeScreenActivity.java
package com.example.homedecorwebshop;

import android.os.Bundle;
import android.widget.ImageView; // Import ImageView
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    private LinearLayout itemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        itemsContainer = findViewById(R.id.itemsContainerLayout);

        List<Item> sampleItems = createSampleItems();
        displayItems(sampleItems);
    }

    private List<Item> createSampleItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Modern Vase", 12000, true, R.drawable.modern_vase, "A sleek and stylish modern vase."));
        // Your updated item:
        items.add(new Item("Rustic Wooden Lamp", 25000, true, R.drawable.wooden_lamp, "Handcrafted rustic lamp with a warm glow."));
        items.add(new Item("Bohemian Wall Tapestry", 8500, false, R.drawable.bohemian_wall_tapestry, "Colorful tapestry to add a bohemian touch."));
        items.add(new Item("Minimalist Desk Organizer", 6000, true, R.drawable.minimalist_desk_organizer, "Keep your desk tidy with this minimalist organizer."));
        return items;
    }

    private void displayItems(List<Item> items) {
        if (itemsContainer == null) return;

        itemsContainer.removeAllViews();

        for (Item item : items) {
            // Create a new LinearLayout for each item to hold its image and text
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL); // Or HORIZONTAL, depending on how you want to arrange
            itemLayout.setPadding(0, 0, 0, 16); // Add some bottom padding between items

            // Create and setup ImageView
            ImageView itemImageView = new ImageView(this);
            itemImageView.setImageResource(item.getImageResourceId());

            // Optional: Set layout parameters for the ImageView (e.g., size)
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                    LinearLayout.LayoutParams.WRAP_CONTENT  // Height - adjust as needed, e.g., 200dp
            );
            imageParams.setMargins(0,0,0,8); // Add some bottom margin to the image
            itemImageView.setLayoutParams(imageParams);
            itemImageView.setAdjustViewBounds(true); // Important to maintain aspect ratio if you set specific dimensions
            // You might want to set a fixed height or use scaleType
            // itemImageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // Example


            // Create and setup TextView
            TextView itemTextView = new TextView(this);
            String itemDetails = item.getName() + "\n" +
                    "Price: " + item.getValue() + " HUF\n" +
                    (item.isInStock() ? "In Stock" : "Out of Stock") + "\n" +
                    "Description: " + item.getDescription();
            itemTextView.setText(itemDetails);
            itemTextView.setTextSize(16f);

            // Add ImageView and TextView to the item's LinearLayout
            itemLayout.addView(itemImageView);
            itemLayout.addView(itemTextView);

            // Add the item's LinearLayout to the main container
            itemsContainer.addView(itemLayout);
        }
    }
}