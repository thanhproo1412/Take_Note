package com.example.take_note;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDiaryActivity extends AppCompatActivity {

    private EditText editTextDiaryTitle, editTextDiaryContent;
    private NoteDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);  // Ensure XML file is named correctly

        // Initialize views
        editTextDiaryTitle = findViewById(R.id.editTextDiaryTitle);
        editTextDiaryContent = findViewById(R.id.editTextDiaryContent);
        dbHelper = new NoteDatabaseHelper(this);  // Initialize database helper

        // Display back button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Handle back button click
    @Override
    public boolean onSupportNavigateUp() {
        finish();  // or onBackPressed();
        return true;
    }

    // Method to save the diary entry
    public void saveDiaryEntry(View view) {
        String title = editTextDiaryTitle.getText().toString().trim();
        String content = editTextDiaryContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            // Validate title and content
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check login status
        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            // Save to the database if user is logged in
            saveToDatabase(title, content);
        } else {
            // Save to SharedPreferences (local storage) if user is not logged in
            saveToLocal(title, content);
        }
    }

    // Method to save the note to the database
    private void saveToDatabase(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);

        long result = db.insert("notes", null, values);
        if (result != -1) {
            Toast.makeText(this, "Diary entry saved to database!", Toast.LENGTH_SHORT).show();
            finish();  // Close activity and return to the previous screen
        } else {
            Toast.makeText(this, "Error saving entry to database", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to save the note to SharedPreferences (local storage)
    private void saveToLocal(String title, String content) {
        SharedPreferences preferences = getSharedPreferences("local_notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int noteCount = preferences.getInt("note_count", 0);  // Get current note count
        editor.putString("note_title_" + noteCount, title);
        editor.putString("note_content_" + noteCount, content);
        editor.putInt("note_count", noteCount + 1);  // Increment the note count

        boolean saved = editor.commit();
        if (saved) {
            Toast.makeText(this, "Diary entry saved locally!", Toast.LENGTH_SHORT).show();
            finish();  // Close activity and return to the previous screen
        } else {
            Toast.makeText(this, "Error saving entry locally", Toast.LENGTH_SHORT).show();
        }
    }
}
