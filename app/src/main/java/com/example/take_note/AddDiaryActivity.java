package com.example.take_note;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDiaryActivity extends AppCompatActivity {

    private EditText editTextDiaryTitle, editTextDiaryContent;
    private Button buttonSave, buttonAttachImage;
    private ImageView imageViewAttachedImage;
    private NoteDatabaseHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        // Initialize views
        editTextDiaryTitle = findViewById(R.id.editTextDiaryTitle);
        editTextDiaryContent = findViewById(R.id.editTextDiaryContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonAttachImage = findViewById(R.id.buttonAttachImage);
        imageViewAttachedImage = findViewById(R.id.imageViewAttachedImage);
        dbHelper = new NoteDatabaseHelper(this);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initially disable the Save button
        buttonSave.setEnabled(false);

        // Add TextWatcher to validate inputs
        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateInputs();
            }
        };

        editTextDiaryTitle.addTextChangedListener(inputWatcher);
        editTextDiaryContent.addTextChangedListener(inputWatcher);

        // Attach click listener to the attach image button
        buttonAttachImage.setOnClickListener(v -> openFilePicker());
    }

    // Validate inputs
    private void validateInputs() {
        String title = editTextDiaryTitle.getText().toString().trim();
        String content = editTextDiaryContent.getText().toString().trim();

        if (title.isEmpty()) {
            editTextDiaryTitle.setError("Title cannot be empty");
        } else {
            editTextDiaryTitle.setError(null);
        }

        if (content.length() < 10) {
            editTextDiaryContent.setError("Content must be at least 10 characters");
        } else {
            editTextDiaryContent.setError(null);
        }

        buttonSave.setEnabled(!title.isEmpty() && content.length() >= 10);
    }

    // Open file picker to select an image
    public void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle result after selecting an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageViewAttachedImage.setVisibility(View.VISIBLE);  // Show ImageView
            imageViewAttachedImage.setImageURI(selectedImageUri);  // Set the selected image to ImageView
        } else {
            Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show();
        }
    }

    // Save the diary entry, including the selected image
    public void saveDiaryEntry(View view) {
        String title = editTextDiaryTitle.getText().toString().trim();
        String content = editTextDiaryContent.getText().toString().trim();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Save to database or local storage based on login status
        if (isLoggedIn()) {
            saveToDatabase(title, content);
        } else {
            saveToLocal(title, content);
        }
    }

    // Check if the user is logged in
    private boolean isLoggedIn() {
        // Implement logic to check login status (e.g., SharedPreferences or session)
        return true; // For now, always true
    }

    // Save the diary entry to the database
    private void saveToDatabase(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("created_at", System.currentTimeMillis());

        long result = db.insert("notes", null, values);
        if (result != -1) {
            Toast.makeText(this, "Diary entry saved to database!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving entry to database", Toast.LENGTH_SHORT).show();
        }
    }

    // Save the diary entry locally
    private void saveToLocal(String title, String content) {
        // Implement logic to save the entry locally (e.g., SharedPreferences)
        Toast.makeText(this, "Diary entry saved locally!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Copy title to clipboard
    public void copyTitleToClipboard(View view) {
        String title = editTextDiaryTitle.getText().toString().trim();
        if (!title.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Diary Title", title);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Title copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Title is empty", Toast.LENGTH_SHORT).show();
        }
    }

    // Copy content to clipboard
    public void copyContentToClipboard(View view) {
        String content = editTextDiaryContent.getText().toString().trim();
        if (!content.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Diary Content", content);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Content copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Content is empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
