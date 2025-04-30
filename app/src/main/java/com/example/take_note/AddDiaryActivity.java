package com.example.take_note;

import android.content.ContentValues;
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
        setContentView(R.layout.activity_add_diary);  // Đảm bảo tên file XML chính xác

        // create views
        editTextDiaryTitle = findViewById(R.id.editTextDiaryTitle);
        editTextDiaryContent = findViewById(R.id.editTextDiaryContent);
        dbHelper = new NoteDatabaseHelper(this); // Khởi tạo đối tượng NoteDatabaseHelper

        // Display back button in Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    // handle when user click back
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // or onBackPressed();
        return true;
    }

    // Method to save note/diary - will update later
    public void saveDiaryEntry(View view) {
        String title = editTextDiaryTitle.getText().toString().trim();
        String content = editTextDiaryContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            // Check title and content
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to db
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);

        // Save to db
        long result = db.insert("notes", null, values);
        if (result != -1) {
            Toast.makeText(this, "Diary entry saved!", Toast.LENGTH_SHORT).show();
            finish();  // close activity, back to previous screen
        } else {
            Toast.makeText(this, "Error saving entry", Toast.LENGTH_SHORT).show();
        }
    }
}
