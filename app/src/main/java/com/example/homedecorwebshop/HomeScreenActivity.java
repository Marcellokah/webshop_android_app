// src/main/java/com/example/homedecorwebshop/HomeScreenActivity.java
package com.example.homedecorwebshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen); // Make sure this matches the layout file name

        Button shopNowButton = findViewById(R.id.shopNowButton);
        shopNowButton.setOnClickListener(v -> {
            // Replace CatalogActivity with the actual activity where you display products
            // startActivity(new Intent(HomeScreenActivity.this, CatalogActivity.class));
        });
    }
}