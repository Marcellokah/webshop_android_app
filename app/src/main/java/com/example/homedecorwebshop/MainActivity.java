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

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextUserPassword);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        mAuth = FirebaseAuth.getInstance();

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
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.i(LOG_TAG, "Attempting login with: " + email + ", password: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(findViewById(R.id.main), "Please enter both email and password.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(LOG_TAG, "signInWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                finish();
            } else {
                Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                String errorMessage = "Authentication failed.";
                if (task.getException() != null) {
                    errorMessage = task.getException().getMessage();
                }
                Snackbar.make(findViewById(R.id.main), errorMessage, Snackbar.LENGTH_LONG).show();
                passwordEditText.setText("");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Showing login UI on startup.");
    }
}