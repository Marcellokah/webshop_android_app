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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * MainActivity serves as the entry point of the application, providing users with
 * options to log in or navigate to the registration screen.
 * <p>
 * This activity handles user authentication using Firebase Authentication.
 * Upon successful login, it navigates the user to the {@link HomeScreenActivity}.
 * If login fails, it displays an appropriate error message.
 * </p>
 */
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;
    private TextInputEditText usernameEditText; // Input field for the user's email
    private TextInputEditText passwordEditText; // Input field for the user's password
    private Button loginButton; // Button to trigger the login process
    private TextView registerTextView; // TextView that acts as a link to the registration screen

    private static final String TAG = "MainActivity"; // Tag for general logging

    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up: create views, bind data to lists, etc. This method also initializes
     * Firebase Authentication and sets up click listeners for the login button and
     * registration text view.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextUserPassword);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        // Initialize Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Set OnClickListener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(); // Call the login method when the button is clicked
            }
        });

        // Set OnClickListener for the registration TextView
        // Navigates to RegistrationActivity when clicked
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
    }

    /**
     * Handles the user login process.
     * Retrieves the email and password from the input fields, validates them,
     * and attempts to sign in the user with Firebase Authentication.
     * <p>
     * If the email or password fields are empty, a {@link Snackbar} is shown.
     * On successful login, it navigates to {@link HomeScreenActivity} and finishes the current activity.
     * On failure, it displays an error message in a {@link Snackbar} and clears the password field.
     * </p>
     */
    private void login() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.i(LOG_TAG, "Attempting login with: " + email); // Password is not logged for security

        // Validate that email and password are not empty
        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(findViewById(R.id.main), "Please enter both email and password.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Attempt to sign in with Firebase
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in success
                Log.d(LOG_TAG, "signInWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser(); // Get the currently signed-in user
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                // Navigate to HomeScreenActivity
                startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                finish(); // Finish MainActivity so the user cannot navigate back to it
            } else {
                // If sign in fails, display a message to the user.
                Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                String errorMessage = "Authentication failed.";
                // Provide more specific error message if available from the exception
                if (task.getException() != null) {
                    errorMessage = task.getException().getMessage();
                }
                Snackbar.make(findViewById(R.id.main), errorMessage, Snackbar.LENGTH_LONG).show();
                passwordEditText.setText(""); // Clear the password field after a failed attempt
            }
        });
    }

    /**
     * Called when the activity is becoming visible to the user.
     * This implementation logs that the login UI is being shown.
     */
    @Override
    public void onStart() {
        super.onStart();
        // It's a good practice to check if the user is already signed in here
        // and update the UI accordingly (e.g., navigate to HomeScreenActivity directly).
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // if(currentUser != null){
        //    startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
        //    finish();
        // }
        Log.d(TAG, "Showing login UI on startup.");
    }
}