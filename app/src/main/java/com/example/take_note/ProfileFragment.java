package com.example.take_note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ProfileFragment extends Fragment {

    private Button btnLogin, btnLogout;
    private ToggleButton btnTestLogin;
    private TextView tvUserName, tvUserEmail, tvUserGender, tvUserDob, tvUserPhone, tvUserAddress;
    private TextView tvNotLoggedIn;
    private View cardProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnTestLogin = view.findViewById(R.id.btnTestLogin);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserGender = view.findViewById(R.id.tvUserGender);
        tvUserDob = view.findViewById(R.id.tvUserDob);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        tvUserAddress = view.findViewById(R.id.tvUserAddress);

        cardProfile = view.findViewById(R.id.cardProfile); // CardView ID in XML
        tvNotLoggedIn = view.findViewById(R.id.tvNotLoggedIn); // TextView ID in XML

        // Load SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        // Update UI
        updateUI(isLoggedIn, sharedPreferences);

        // Login button
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.clear();  // Clear all user data
            editor.apply();
            updateUI(false, sharedPreferences);
        });

        // Toggle login (test button)
        btnTestLogin.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            boolean isCurrentlyLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (isCurrentlyLoggedIn) {
                // Simulate logout
                editor.putBoolean("isLoggedIn", false);
                editor.clear();
            } else {
                // Simulate login with dummy data
                editor.putBoolean("isLoggedIn", true);
                editor.putString("userName", "John Doe");
                editor.putString("userEmail", "john.doe@example.com");
                editor.putString("userGender", "Male");
                editor.putString("userDob", "01/01/1990");
                editor.putString("userPhone", "0123456789");
                editor.putString("userAddress", "123 Main Street");
            }

            editor.apply();
            updateUI(!isCurrentlyLoggedIn, sharedPreferences);
        });

        return view;
    }

    private void updateUI(boolean isLoggedIn, SharedPreferences sharedPreferences) {
        if (isLoggedIn) {
            // Get data
            String userName = sharedPreferences.getString("userName", "Unknown");
            String userEmail = sharedPreferences.getString("userEmail", "Unknown");
            String gender = sharedPreferences.getString("userGender", "N/A");
            String dob = sharedPreferences.getString("userDob", "N/A");
            String phone = sharedPreferences.getString("userPhone", "N/A");
            String address = sharedPreferences.getString("userAddress", "N/A");

            // Set data
            tvUserName.setText(userName);
            tvUserEmail.setText(userEmail);
            tvUserGender.setText("Gender: " + gender);
            tvUserDob.setText("DOB: " + dob);
            tvUserPhone.setText("Phone: " + phone);
            tvUserAddress.setText("Address: " + address);

            // Show profile info
            cardProfile.setVisibility(View.VISIBLE);
            tvNotLoggedIn.setVisibility(View.GONE);

            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            // Hide profile and show message
            cardProfile.setVisibility(View.GONE);
            tvNotLoggedIn.setVisibility(View.VISIBLE);

            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }
}
