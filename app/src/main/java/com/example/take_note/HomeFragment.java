package com.example.take_note;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private Button btnSelectDate;
    private TextView tvSelectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        tvSelectedDate = view.findViewById(R.id.tvNoNotesMessage); // You can update this to any TextView to display selected date

        // Set the OnClickListener for the "Select Date" button
        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());

        return view;
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth1) -> {
                    // Format and display the selected date
                    String selectedDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                    tvSelectedDate.setText("Selected Date: " + selectedDate);
                },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }
}
