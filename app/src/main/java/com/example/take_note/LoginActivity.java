package com.example.take_note;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputLayoutUsername;
    private TextInputLayout inputLayoutPassword;
    private TextInputEditText editTextUsername;
    private TextInputEditText editTextPassword;
    private MaterialButton buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        inputLayoutUsername = findViewById(R.id.inputLayoutUsername);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Display back button in Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set up login button click listener
        buttonLogin.setOnClickListener(v -> handleLogin());
    }

    // Handle Login logic
    private void handleLogin() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            // Show error messages if fields are empty
            if (username.isEmpty()) {
                inputLayoutUsername.setError("Username is required");
            } else {
                inputLayoutUsername.setError(null);
            }

            if (password.isEmpty()) {
                inputLayoutPassword.setError("Password is required");
            } else {
                inputLayoutPassword.setError(null);
            }
        } else {
            // Handle successful login
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            // Proceed to main app activity (navigate to another activity or fragment)
        }
    }

    // Handle when user clicks back
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
