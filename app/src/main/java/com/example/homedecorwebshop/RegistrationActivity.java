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

public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getName();

    private TextInputEditText emailEditText;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button registerButton;

    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_registration);

        TextInputLayout emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = (TextInputEditText) emailInputLayout.getEditText();

        TextInputLayout usernameInputLayout = findViewById(R.id.usernameInputLayout);
        usernameEditText = (TextInputEditText) usernameInputLayout.getEditText(); // Keeping this for now, you can decide later how to use it

        TextInputLayout passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = (TextInputEditText) passwordInputLayout.getEditText();

        TextInputLayout confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        confirmPasswordEditText = (TextInputEditText) confirmPasswordInputLayout.getEditText();

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

        Log.i(LOG_TAG, "Attempting registration with: " + email); // Removed username from log

        // Basic input validation (as before)
        if (!isValidEmail(email) || password.isEmpty() || !password.equals(confirmPassword)) {
            String errorMessage = "Invalid input. Please check your fields.";
            if (!isValidEmail(email)) {
                errorMessage = "Invalid email address.";
            } else if (password.isEmpty()) {
                errorMessage = "Password cannot be empty.";
            } else if (!password.equals(confirmPassword)) {
                errorMessage = "Passwords do not match.";
            }
            Snackbar.make(findViewById(R.id.main), errorMessage, Snackbar.LENGTH_SHORT).show();
            return; // Stop registration if validation fails
        }

        // ** Firebase Registration **
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Registration success
                    Log.d(LOG_TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the login activity.

                } else {
                    // Registration failed
                    Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                    String errorMessage = "Registration failed: " + task.getException().getMessage();
                    Snackbar.make(findViewById(R.id.main), errorMessage, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}