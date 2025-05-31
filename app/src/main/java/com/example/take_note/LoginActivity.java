package com.example.take_note;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import android.content.SharedPreferences;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputLayoutUsername;
    private TextInputLayout inputLayoutPassword;
    private TextInputEditText editTextUsername;
    private TextInputEditText editTextPassword;
    private MaterialButton buttonLogin;
    private MaterialButton buttonSignUp;
    private MaterialButton buttonForgotPassword;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new UserDatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        inputLayoutUsername = findViewById(R.id.inputLayoutUsername);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);

        // Display back button in Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set click listeners
        buttonLogin.setOnClickListener(v -> handleLogin());
        buttonSignUp.setOnClickListener(v -> navigateToSignUp());
        buttonForgotPassword.setOnClickListener(v -> navigateToForgotPassword());
    }

    // Handle Login logic
    private void handleLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Reset error messages
        inputLayoutUsername.setError(null);
        inputLayoutPassword.setError(null);

        boolean isValid = true;

        if (username.isEmpty()) {
            inputLayoutUsername.setError("Username is required");
            isValid = false;
        }

        if (password.isEmpty()) {
            inputLayoutPassword.setError("Password is required");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Check credentials using SQLite
        boolean isAuthenticated = dbHelper.checkUserCredentials(username, password);

        if (isAuthenticated) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

            // Giả sử bạn có thể lấy thông tin user từ database, hoặc tạm thời dùng dummy data
            // Ví dụ lấy thông tin user từ database helper (bạn cần tự hiện thực method này)
            User user = dbHelper.getUserByUsername(username);

            SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("is_logged_in", true);
            editor.putString("userName", user.getName() != null ? user.getName() : username);
            editor.putString("userEmail", user.getEmail() != null ? user.getEmail() : "");
            editor.putString("userGender", user.getGender() != null ? user.getGender() : "N/A");
            editor.putString("userDob", user.getDob() != null ? user.getDob() : "N/A");
            editor.putString("userPhone", user.getPhone() != null ? user.getPhone() : "N/A");
            editor.putString("userAddress", user.getAddress() != null ? user.getAddress() : "N/A");

            // Các tùy chọn mặc định
            editor.putBoolean("cbReceiveEmail", false);
            editor.putBoolean("cbReceiveSms", false);
            editor.putBoolean("cbDarkMode", false);
            editor.putInt("emotionLevel", 50);

            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle Sign Up button click
    private void navigateToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    // Handle Forgot Password button click
    private void navigateToForgotPassword() {
        Toast.makeText(this, "Navigate to Forgot Password", Toast.LENGTH_SHORT).show();
        // Optional: startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
