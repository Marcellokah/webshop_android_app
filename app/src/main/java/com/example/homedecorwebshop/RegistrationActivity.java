package com.example.homedecorwebshop;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * RegistrationActivity provides a user interface for new users to register
 * for an account using their email and password.
 * <p>
 * This activity handles user input validation and uses Firebase Authentication
 * to create new user accounts. Upon successful registration, it displays a confirmation
 * message and finishes, typically returning the user to the login screen.
 * If registration fails, it displays an appropriate error message.
 * </p>
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private TextInputEditText emailEditText; // Input field for the user's email
    private TextInputEditText usernameEditText; // Input field for the user's desired username (currently not used for Firebase Auth directly)
    private TextInputEditText passwordEditText; // Input field for the user's password
    private TextInputEditText confirmPasswordEditText; // Input field for password confirmation
    private Button registerButton; // Button to trigger the registration process

    private FirebaseAuth mAuth; // Firebase Authentication instance

    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up: create views, bind data to lists, etc. This method also initializes
     * Firebase Authentication and sets up a click listener for the registration button.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_registration);

        // Initialize UI elements by finding their respective views and extracting the TextInputEditText
        TextInputLayout emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = (TextInputEditText) emailInputLayout.getEditText();

        TextInputLayout usernameInputLayout = findViewById(R.id.usernameInputLayout);
        usernameEditText = (TextInputEditText) usernameInputLayout.getEditText();

        TextInputLayout passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = (TextInputEditText) passwordInputLayout.getEditText();

        TextInputLayout confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        confirmPasswordEditText = (TextInputEditText) confirmPasswordInputLayout.getEditText();

        registerButton = findViewById(R.id.registerButton);

        // Set OnClickListener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(); // Call the registerUser method when the button is clicked
            }
        });
    }

    /**
     * Handles the user registration process.
     * Retrieves email, username, password, and confirmation password from the input fields.
     * Validates the input:
     * <ul>
     *     <li>Checks for a valid email format.</li>
     *     <li>Ensures the password is not empty.</li>
     *     <li>Verifies that the password and confirmation password match.</li>
     * </ul>
     * If validation fails, a {@link Snackbar} with an appropriate error message is shown.
     * If validation passes, it attempts to create a new user account with Firebase Authentication
     * using the provided email and password.
     * <p>
     * On successful account creation, a success {@link Toast} is displayed, and the activity is finished.
     * On failure, a {@link Snackbar} with the error message from Firebase is shown.
     * </p>
     */
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim(); // Username is collected but not directly used for Firebase email/password auth
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        Log.i(LOG_TAG, "Attempting registration with: " + email);

        // Validate user input
        if (!isValidEmail(email) || password.isEmpty() || !password.equals(confirmPassword)) {
            String errorMessage = "Invalid input. Please check your fields."; // Default error
            if (!isValidEmail(email)) {
                errorMessage = "Invalid email address.";
            } else if (password.isEmpty()) {
                errorMessage = "Password cannot be empty.";
            } else if (!password.equals(confirmPassword)) {
                errorMessage = "Passwords do not match.";
            }
            // Show error message in a Snackbar. R.id.main should be the ID of a root layout in activity_registration.xml
            Snackbar.make(findViewById(R.id.main), errorMessage, Snackbar.LENGTH_SHORT).show();
            return; // Stop the registration process if validation fails
        }

        // Attempt to create a user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Log.d(LOG_TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser(); // Get the newly created user

                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    finish(); // Finish RegistrationActivity and return to the previous screen (likely MainActivity)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                    String errorMessage = "Registration failed.";
                    if (task.getException() != null && task.getException().getMessage() != null) {
                        errorMessage = "Registration failed: " + task.getException().getMessage();
                    }
                    // Show error message in a Snackbar
                    Snackbar.make(findViewById(R.id.main), errorMessage, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Validates the format of an email address.
     *
     * @param email The email string to validate.
     * @return {@code true} if the email address is in a valid format, {@code false} otherwise.
     */
    private boolean isValidEmail(String email) {
        // Use Android's built-in Patterns.EMAIL_ADDRESS matcher for email validation
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}