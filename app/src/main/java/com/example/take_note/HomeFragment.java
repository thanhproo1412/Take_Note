package com.example.take_note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> noteList;
    private NoteDatabaseHelper dbHelper;
    private FloatingActionButton fabAddEntry;
    private TextView tvNoNotesMessage;
    private TextView tvLoginMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        tvLoginMessage = view.findViewById(R.id.tvLoginMessage);
        tvNoNotesMessage = view.findViewById(R.id.tvNoNotesMessage);
        fabAddEntry = view.findViewById(R.id.fab_add_entry);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize database and note list
        dbHelper = new NoteDatabaseHelper(getContext());
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

        // Check login status and load notes
        checkLoginStatus();

        // Floating action button click to add a new note
        fabAddEntry.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddDiaryActivity.class);
            startActivity(intent);
        });

        // Navigate to LoginActivity when user clicks "Click here to login"
        tvLoginMessage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void checkLoginStatus() {
        if (!isLoggedIn()) {
            tvLoginMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tvNoNotesMessage.setVisibility(View.GONE);
        } else {
            tvLoginMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            loadNotes();
        }
    }

    private boolean isLoggedIn() {
        return getActivity().getSharedPreferences("user_preferences", getContext().MODE_PRIVATE)
                .getBoolean("is_logged_in", false);
    }

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
}
