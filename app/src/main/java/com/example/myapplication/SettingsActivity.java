package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.ViewModels.BaseUrlInterceptor;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        RelativeLayout mainLayout = findViewById(R.id.main_layout);
        mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.background));

        // Assign view elements to variables
        EditText baseUrlEditText = findViewById(R.id.baseUrl);
        Button saveButton = findViewById(R.id.saveButton);
        Button darkLightModeButton = findViewById(R.id.darkLightModeButton);

        // Load saved base URL, if it exists
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String savedBaseUrl = prefs.getString("baseUrl", "http://10.0.2.2:5000/api/");
        baseUrlEditText.setText(savedBaseUrl);

        // Save base URL on save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newBaseUrl = baseUrlEditText.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("AppSettings", MODE_PRIVATE).edit();
                editor.putString("baseUrl", newBaseUrl);
                editor.apply();
                BaseUrlInterceptor.getInstance().setBaseUrl(newBaseUrl);
            }
        });

        // Toggle between dark and light mode
        darkLightModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentMode = AppCompatDelegate.getDefaultNightMode();
                int newMode = currentMode == AppCompatDelegate.MODE_NIGHT_YES ?
                        AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;

                AppCompatDelegate.setDefaultNightMode(newMode);
                recreate();
            }
        });
    }
}
