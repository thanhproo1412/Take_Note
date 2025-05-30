package com.example.take_note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private ArrayList<Note> noteList;
    private NoteDatabaseHelper dbHelper;
    private FloatingActionButton fabAddEntry;
    private TextView tvNoNotesMessage;
    private TextView tvLoginMessage;

    // Declare buttons and ProgressBar
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        tvLoginMessage = view.findViewById(R.id.tvLoginMessage);
        tvNoNotesMessage = view.findViewById(R.id.tvNoNotesMessage);
        fabAddEntry = view.findViewById(R.id.fab_add_entry);
        progressBar = view.findViewById(R.id.progressBar);

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
        // Show ProgressBar while loading notes
        progressBar.setVisibility(View.VISIBLE);

        noteList.clear(); // Clear the current list
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("notes", null, null, null, null, null, "id DESC");

        if (cursor.getCount() == 0) {
            tvNoNotesMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE); // Hide ProgressBar when no notes
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
            progressBar.setVisibility(View.GONE); // Hide ProgressBar after loading notes
        }
    }

    // Show DatePickerDialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Show TimePickerDialog
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    String selectedTime = hourOfDay + ":" + minute;
                    Toast.makeText(getContext(), "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    // Show DateTimePickerDialog
    private void showDateTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    String selectedTime = hourOfDay + ":" + minute;
                    Toast.makeText(getContext(), "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        datePickerDialog.show();
        timePickerDialog.show();
    }
}
