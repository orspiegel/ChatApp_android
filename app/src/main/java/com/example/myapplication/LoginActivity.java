package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DB.UsersDB;
import com.example.myapplication.Dao.UserDao;
import com.example.myapplication.api.UserAPI;


public class LoginActivity extends AppCompatActivity implements UserAPI.TokenCallback{
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
//        TextView loginMsg =

        userDB = UsersDB.getInstance(this);
        userDao = userDB.userDao();


        Button loginButton = findViewById(R.id.login_button);

        userAPI = new UserAPI(userDao);
//        userAPI.getAllUsers();
        loginButton.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();
            userAPI.getToken(enteredUsername, enteredPassword, this);
//            String savedToken = Utils.retrieveTokenFromSharedPreferences();
            String savedToken = MyApplication.getToken();
            Log.d("LoginActivity","Token is "+savedToken);
//            userAPI.login(enteredUsername, enteredPassword, savedToken);


//            userAPI.logInWithToken(enteredUsername, enteredPassword);
//
//            // Check if username and password are entered
//            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
//            } else {
//                new Thread(()-> {
//                    loggedIn = userDao.getUserByUsernameAndPassword(enteredUsername, enteredPassword);
//                    if (loggedIn != null) {
//                        // User login successful
//                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
//
//                        // TODO: Start the main chat activity or any other desired activity
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
//                    } else {
//                        // Invalid username or password
//                        Log.d("LoginActivity", "logged in is null");
////                        Toast.makeText(LoginActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
//                    }
//
//                }).start();
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            Log.d("LoginActivity", "registering");
            // navigate to Register Activity
            startActivity(new Intent(LoginActivity.this,
                    com.example.myapplication.MainActivity.class));
            finish();
        });
    }

    @Override
    public void onTokenReceived(String token) {
        Log.d("LoginActivity", "Token received is " + token);
        String enteredUsername = username.getText().toString().trim();
        String enteredPassword = password.getText().toString().trim();
        userAPI.login(enteredUsername, enteredPassword, token);


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
//    private String retrieveTokenFromSharedPreferences() {
//        SharedPreferences sharedPreferences = MyApplication.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        return sharedPreferences.getString("token", null);
//    }


}