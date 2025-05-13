package com.example.take_note;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDiaryActivity extends AppCompatActivity {

    private EditText editTextDiaryTitle, editTextDiaryContent;
    private Button buttonSave;
    private NoteDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        // Initialize views
        editTextDiaryTitle = findViewById(R.id.editTextDiaryTitle);
        editTextDiaryContent = findViewById(R.id.editTextDiaryContent);
        buttonSave = findViewById(R.id.buttonSave);
        dbHelper = new NoteDatabaseHelper(this);

        // Display back button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initially disable the Save button
        buttonSave.setEnabled(false);

        // Add TextWatcher to validate input
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
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void saveDiaryEntry(View view) {
        String title = editTextDiaryTitle.getText().toString().trim();
        String content = editTextDiaryContent.getText().toString().trim();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            saveToDatabase(title, content);
        } else {
            saveToLocal(title, content);
        }
    }

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

    private void saveToLocal(String title, String content) {
        SharedPreferences preferences = getSharedPreferences("local_notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int noteCount = preferences.getInt("note_count", 0);
        editor.putString("note_title_" + noteCount, title);
        editor.putString("note_content_" + noteCount, content);
        editor.putLong("note_time_" + noteCount, System.currentTimeMillis());
        editor.putInt("note_count", noteCount + 1);

        boolean saved = editor.commit();
        if (saved) {
            Toast.makeText(this, "Diary entry saved locally!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving entry locally", Toast.LENGTH_SHORT).show();
        }
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
}
