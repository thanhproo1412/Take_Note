package com.example.take_note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private Button btnLogin, btnLogout;
    private ToggleButton btnTestLogin;
    private TextView tvUserName, tvUserEmail, tvUserGender, tvUserDob, tvUserPhone, tvUserAddress;
    private TextView tvNotLoggedIn, tvEmotionLevel;
    private View cardProfile;
    private CheckBox cbReceiveEmail, cbReceiveSms, cbDarkMode;
    private SeekBar seekBarEmotion;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        cbReceiveEmail = view.findViewById(R.id.cbReceiveEmail);
        cbReceiveSms = view.findViewById(R.id.cbReceiveSms);
        cbDarkMode = view.findViewById(R.id.cbDarkMode);

        cardProfile = view.findViewById(R.id.cardProfile);
        tvNotLoggedIn = view.findViewById(R.id.tvNotLoggedIn);

        tvEmotionLevel = view.findViewById(R.id.tvEmotionLevel);
        seekBarEmotion = view.findViewById(R.id.seekBarEmotion);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        updateUI(isLoggedIn, sharedPreferences);

        // Login
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            updateUI(false, sharedPreferences);
        });

        // Test login
        btnTestLogin.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            boolean isCurrentlyLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (isCurrentlyLoggedIn) {
                editor.clear(); // logout
            } else {
                // dummy login
                editor.putBoolean("isLoggedIn", true);
                editor.putString("userName", "John Doe");
                editor.putString("userEmail", "john.doe@example.com");
                editor.putString("userGender", "Male");
                editor.putString("userDob", "01/01/1990");
                editor.putString("userPhone", "0123456789");
                editor.putString("userAddress", "123 Main Street");
                editor.putBoolean("cbReceiveEmail", true);
                editor.putBoolean("cbReceiveSms", false);
                editor.putBoolean("cbDarkMode", false);
                editor.putInt("emotionLevel", 50); // Default level
            }

            editor.apply();
            updateUI(!isCurrentlyLoggedIn, sharedPreferences);
        });

        // Checkbox listeners
        cbReceiveEmail.setOnCheckedChangeListener((buttonView, isChecked) ->
                sharedPreferences.edit().putBoolean("cbReceiveEmail", isChecked).apply()
        );

        cbReceiveSms.setOnCheckedChangeListener((buttonView, isChecked) ->
                sharedPreferences.edit().putBoolean("cbReceiveSms", isChecked).apply()
        );

        cbDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show confirmation dialog
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Enable Dark Mode")
                        .setMessage("Are you sure you want to enable dark mode?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            sharedPreferences.edit().putBoolean("cbDarkMode", true).apply();
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            cbDarkMode.setChecked(false); // Revert the change
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .show();
            } else {
                // Directly disable without confirmation
                sharedPreferences.edit().putBoolean("cbDarkMode", false).apply();
            }
        });


        // SeekBar listener
        seekBarEmotion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvEmotionLevel.setText("Emotion Sharing Level: " + progress);
                sharedPreferences.edit().putInt("emotionLevel", progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
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
            int emotionLevel = sharedPreferences.getInt("emotionLevel", 50);

            // Set data
            tvUserName.setText(userName);
            tvUserEmail.setText(userEmail);
            tvUserGender.setText("Gender: " + gender);
            tvUserDob.setText("DOB: " + dob);
            tvUserPhone.setText("Phone: " + phone);
            tvUserAddress.setText("Address: " + address);
            cbReceiveEmail.setChecked(sharedPreferences.getBoolean("cbReceiveEmail", false));
            cbReceiveSms.setChecked(sharedPreferences.getBoolean("cbReceiveSms", false));
            cbDarkMode.setChecked(sharedPreferences.getBoolean("cbDarkMode", false));
            tvEmotionLevel.setText("Emotion Sharing Level: " + emotionLevel);
            seekBarEmotion.setProgress(emotionLevel);

            // Show profile
            cardProfile.setVisibility(View.VISIBLE);
            tvNotLoggedIn.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            cardProfile.setVisibility(View.GONE);
            tvNotLoggedIn.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }
}
