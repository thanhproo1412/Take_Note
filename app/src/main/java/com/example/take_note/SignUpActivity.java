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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize EditText views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // Handle the touch event on the username field to show the character picker dialog
        editTextUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2; // Index for drawableRight
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextUsername.getRight() - editTextUsername.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        showCharacterPicker();
                        return true;
                    }
                }
                return false;
            }
        });

        // Initialize Sign Up button
        findViewById(R.id.btnSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        // Initialize Login button
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    // Handle sign-up action
    private void signup() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Clear previous error messages
        editTextUsername.setError(null);
        editTextEmail.setError(null);
        editTextPassword.setError(null);
        editTextConfirmPassword.setError(null);

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            // Proceed with sign-up process (e.g., save to database or server)
            Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
        }
    }

    // Navigate to Login screen
    private void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // Show CharacterPickerDialog when user clicks on the username field icon
    private void showCharacterPicker() {
        // Characters to choose from
        final String specialChars = "@#$%&*";

        // Create a simple alert dialog to pick characters
        final CharSequence[] items = specialChars.split("");

        new android.app.AlertDialog.Builder(this)
                .setTitle("Pick a Special Character")
                .setItems(items, (dialog, which) -> {
                    // Insert the selected character at the current cursor position in username field
                    String selectedChar = items[which].toString();
                    int cursorPos = editTextUsername.getSelectionStart();
                    Editable text = editTextUsername.getText();
                    text.insert(cursorPos, selectedChar);
                })
                .show();
    }
}
