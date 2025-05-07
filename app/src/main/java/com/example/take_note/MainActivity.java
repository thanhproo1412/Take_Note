package com.example.take_note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> noteList;
    private NoteDatabaseHelper dbHelper;
    private FloatingActionButton fabAddEntry;
    private TextView tvNoNotesMessage;
    private TextView tvLoginMessage;
    private DrawerLayout drawerLayout; // DrawerLayout to manage the sidebar
    private ActionBarDrawerToggle toggle; // ActionBarDrawerToggle to toggle sidebar
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Xử lý sự kiện khi người dùng chọn mục
        // Inside your MainActivity class
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the item ID

                if (itemId == R.id.nav_home) {
                    // Handle Home navigation
                    Toast.makeText(MainActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_notes) {
                    // Handle Notes navigation
                    Toast.makeText(MainActivity.this, "Notes selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Handle Profile navigation
                    Toast.makeText(MainActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewNotes);
        tvLoginMessage = findViewById(R.id.tvLoginMessage);
        tvNoNotesMessage = findViewById(R.id.tvNoNotesMessage);
        fabAddEntry = findViewById(R.id.fab_add_entry);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database and note list
        dbHelper = new NoteDatabaseHelper(this);
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

        // Set up ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the hamburger icon
        }

        // Check login status and load notes
        checkLoginStatus();

        // Floating action button click to add a new note
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
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true; // Handle drawer toggle
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoggedIn()) {
            loadNotes();
        }
    }

    // Check login status and display notes accordingly
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

    // Check if the user is logged in using SharedPreferences
    private boolean isLoggedIn() {
        return getSharedPreferences("user_preferences", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);
    }

    // Load notes from the database
    private void loadNotes() {
        noteList.clear(); // Clear the current list
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("notes", null, null, null, null, null, "id DESC");

        if (cursor.getCount() == 0) {
            tvNoNotesMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
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

    @Override
    public void onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return; // Prevent calling super.onBackPressed()
            }
        }
        super.onBackPressed(); // Call the default back button behavior
    }

}
