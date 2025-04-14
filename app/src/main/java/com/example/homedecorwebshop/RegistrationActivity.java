package com.example.homedecorwebshop;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getName();

    private TextInputEditText emailEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        Log.i(LOG_TAG, "Attempting registration with: " + email + ", " + username);

        // Basic input validation
        if (!isValidEmail(email) || username.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
            String errorMessage = "Invalid input. Please check your fields.";
            if (!isValidEmail(email)) {
                errorMessage = "Invalid email address.";
            } else if (username.isEmpty()) {
                errorMessage = "Username cannot be empty.";
            } else if (password.isEmpty()) {
                errorMessage = "Password cannot be empty.";
            } else if (!password.equals(confirmPassword)) {
                errorMessage = "Passwords do not match.";
            }
            Snackbar.make(findViewById(R.id.main), errorMessage, Snackbar.LENGTH_SHORT).show();
            return; // Stop registration if validation fails
        }

        // **  Replace this with your actual registration logic (e.g., sending data to a server) **
        // For this example, we'll just log a success message and show a toast.
        // In a real app, you'd likely use Retrofit or Volley to make an API call.

        Log.i(LOG_TAG, "Registration successful (placeholder - implement real logic!)");
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

        // After successful registration, you might:
        // 1. Navigate to the login screen (and finish this activity)
        //    finish(); // Close the registration activity
        // 2. Or, automatically log the user in and go to the main screen:
        //    startActivity(new Intent(this, HomeScreenActivity.class));  // Replace with your main activity
        //    finish();

    }

    // Basic email validation (you might want a more robust check)
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}