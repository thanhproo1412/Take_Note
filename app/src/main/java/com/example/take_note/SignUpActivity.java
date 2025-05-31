package com.example.take_note;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize database helper
        dbHelper = new UserDatabaseHelper(this);

        // Initialize input fields
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // Set up special character picker on username field icon
        editTextUsername.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= (editTextUsername.getRight()
                            - editTextUsername.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                showCharacterPicker();
                return true;
            }
            return false;
        });

        // Set up sign-up button
        findViewById(R.id.btnSignup).setOnClickListener(v -> signup());

        // Set up redirect to login
        findViewById(R.id.btnLogin).setOnClickListener(v -> navigateToLogin());
    }

    /**
     * Handles the signup logic: validation, duplicate check, DB insertion
     */
    private void signup() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Clear previous errors
        editTextUsername.setError(null);
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextConfirmPassword.setError(null);

        boolean isValid = true;

        // Basic input validation
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            isValid = false;
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Confirm your password");
            isValid = false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        if (!isValid) return;

        // Initialize DB helper
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);

        // Check if username already exists
        if (dbHelper.isUsernameExists(username)) {
            Toast.makeText(this, "Username already exists. Choose another.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert user into DB
        boolean isInserted = dbHelper.insertUser(username, email, password);
        if (isInserted) {
            Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
            navigateToLogin(); // Redirect to Login
        } else {
            Toast.makeText(this, "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigates the user to the LoginActivity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Shows a dialog allowing the user to insert a special character into the username
     */
    private void showCharacterPicker() {
        final String specialChars = "@#$%&*";
        final CharSequence[] items = specialChars.split("");

        new android.app.AlertDialog.Builder(this)
                .setTitle("Pick a Special Character")
                .setItems(items, (dialog, which) -> {
                    String selectedChar = items[which].toString();
                    int cursorPos = editTextUsername.getSelectionStart();
                    Editable text = editTextUsername.getText();
                    text.insert(cursorPos, selectedChar);
                })
                .show();
    }
}
