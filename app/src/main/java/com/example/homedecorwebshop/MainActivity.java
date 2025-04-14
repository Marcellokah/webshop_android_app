package com.example.homedecorwebshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextUserPassword);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

    }

    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.i(LOG_TAG, "Attempting login with: " + username + ", password: " + password);

        //  ** Replace this with your actual authentication logic **
        if (username.equals("admin") && password.equals("password")) {
            // Successful login:
            Log.i(LOG_TAG, "Login successful");
            // Example: navigate to the main app screen
            // startActivity(new Intent(this, HomeScreenActivity.class));  // Replace with your actual main activity
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            finish(); // Close the login activity (if appropriate for your app flow)

        } else {
            // Login failed:
            Log.i(LOG_TAG, "Login failed");
            Snackbar.make(findViewById(R.id.main), "Invalid username or password.", Snackbar.LENGTH_SHORT).show();
            // Optional: Clear the password field after a failed attempt
            passwordEditText.setText("");
        }
    }
}