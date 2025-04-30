package com.example.take_note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> noteList;
    private NoteDatabaseHelper dbHelper;
    private FloatingActionButton fabAddEntry;
    private TextView tvNoNotesMessage;
    private TextView tvLoginMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create views
        recyclerView = findViewById(R.id.recyclerViewNotes);
        tvLoginMessage = findViewById(R.id.tvLoginMessage);
        tvNoNotesMessage = findViewById(R.id.tvNoNotesMessage);
        fabAddEntry = findViewById(R.id.fab_add_entry);

        //  RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create diary list
        dbHelper = new NoteDatabaseHelper(this);
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

        // Check login status and diary data
        checkLoginStatus();

        // Func when click button +
        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AddDiaryActivity.class);
                    startActivity(intent);
            }
        });

        // Navigate to LoginActivity when user clicks "Click here to login"
        tvLoginMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoggedIn()) {
            loadNotes();
        }
    }

    // Check login status and diary data to display to screen
    private void checkLoginStatus() {
        if (!isLoggedIn()) {
            tvLoginMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tvNoNotesMessage.setVisibility(View.GONE);
        } else {
            tvLoginMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // using SharedPreferences to check
    private boolean isLoggedIn() {
        return getSharedPreferences("user_preferences", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);
    }

    // Load data from db
    private void loadNotes() {
        noteList.clear();  // clear current list
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("notes", null, null, null, null, null, "id DESC");

        if (cursor.getCount() == 0) {
            // if there is no note, display msg "tvNoNotesMessage"
            tvNoNotesMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // if have any note, display it
            tvNoNotesMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                noteList.add(new Note(id, title, content));
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        }
    }
}
