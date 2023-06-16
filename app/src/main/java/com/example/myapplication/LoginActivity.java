package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    String usernameValue = username.getText().toString();
                    String passwordValue = password.getText().toString();

                    URL url = new URL("http://10.0.2.2:5000/api/Tokens");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", usernameValue);
                    jsonParam.put("password", passwordValue);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    if(conn.getResponseCode() == 200) {
                        InputStream in = new BufferedInputStream(conn.getInputStream());
                        String token = readStream(in);

                        // Print the token to logcat
                        Log.d("LoginActivity", "Token: " + token);

                        // Store token in shared preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("myapplication", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.apply();

                        UserDatabaseHelper dbHelper = new UserDatabaseHelper(LoginActivity.this);
                        dbHelper.updateUserAuthentication(usernameValue, true);
                        Cursor res = dbHelper.getAllData();
                        while (res.moveToNext()) {
                            String userData = "Row ID: " + res.getString(0) +
                                    " Username: " + res.getString(1) +
                                    " Password: " + res.getString(2) +
                                    " isAuthenticated: " + res.getString(3) +
                                    " Contact List: " + res.getString(4);
                            Log.d("DBData", userData);
                        }
                        // Navigate to other activity
                        Intent intent = new Intent(LoginActivity.this, com.example.myapplication.MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        System.out.println("Login failed: " + conn.getResponseMessage());
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            // navigate to Register Activity
            startActivity(new Intent(LoginActivity.this, com.example.myapplication.MainActivity.class));
            finish();
        });
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}