package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DB.UsersDB;
import com.example.myapplication.Dao.UserDao;
import com.example.myapplication.api.UserAPI;


public class LoginActivity extends AppCompatActivity implements UserAPI.TokenCallback {
    private EditText username;
    private EditText password;
    private UsersDB userDB;
    private UserDao userDao;
    private UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        userDB = UsersDB.getInstance(this);
        userDao = userDB.userDao();

        Button loginButton = findViewById(R.id.login_button);

        userAPI = new UserAPI(userDao);

        loginButton.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();
            userAPI.getToken(enteredUsername, enteredPassword, this);
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            Log.d("LoginActivity", "registering");
            // Navigate to Register Activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
    public void onTokenReceived(String token) {
        Log.d("LoginActivity", "Token received is " + token);
        String enteredUsername = username.getText().toString().trim();
        String enteredPassword = password.getText().toString().trim();
        userAPI.login(enteredUsername, enteredPassword, token, this);
    }

    public void onLoginSuccess(Context context, Class<ChatListActivity> activityClass) {
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.context.startActivity(intent);

    }
}
