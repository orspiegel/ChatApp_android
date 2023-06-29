package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.myapplication.DB.UsersDB;
import com.example.myapplication.Dao.UserDao;
import com.example.myapplication.Entites.User;
//import com.example.myapplication.ViewModels.BaseUrlInterceptor;
import com.example.myapplication.api.UserAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;
public class MainActivity extends AppCompatActivity {
    private ImageView avatar;
    private EditText username;
    private EditText nickname;
    private EditText password;
    private EditText confirmPassword;
    private Bitmap selectedImage;
    private UserDao userDao;

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            avatar.setImageURI(selectedImageUri);
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.log_in_button);
        UsersDB database = UsersDB.getInstance(this);
        userDao = database.userDao();

        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        Button registerButton = findViewById(R.id.register_button2);

        avatar.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mGetContent.launch(Intent.createChooser(intent, "Select Picture"));
        });
        loginButton.setOnClickListener(v -> { // This is the code block you asked about
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> {
            try {
                String usernameValue = username.getText().toString();
                String displayNameValue = nickname.getText().toString();
                String passwordValue = password.getText().toString();
                String confirmPasswordValue = confirmPassword.getText().toString();

                if (usernameValue.contains(" ")) {
                    Toast.makeText(MainActivity.this, "Please enter a valid username - no space, tab or new-line characters allowed.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isPasswordValid(passwordValue)) {
                    Toast.makeText(MainActivity.this, "Password must be at least 8 characters long and contain letters and numbers.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!passwordValue.equals(confirmPasswordValue)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (displayNameValue.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a valid display name - must contain at least one letter or number.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (selectedImage == null) {
                    Toast.makeText(MainActivity.this, "Please select an image.", Toast.LENGTH_LONG).show();
                    return;
                }

                String profilePicValue = encodeImage(selectedImage);

                // Save user to SQLite database
                User user = new User(usernameValue, displayNameValue, passwordValue, profilePicValue);

                // Initialize UserAPI
                UserAPI userAPI = new UserAPI(userDao);

                new Thread(() -> {
                    // Insert User object into SQLite database
                    userDao.insert(user);
                    Log.d("UserDB", "User added successfully.");

                    // Fetch all users and log them
                    List<User> users = userDao.getAllUsersSync();
                    for (User fetchedUser : users) {
                        Log.d("UserDB", "User: " + fetchedUser.getUserName());
                    }

                    // Register the user on the server
                    userAPI.addUser(user);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    });
                }).start();

            } catch (Exception e) {
                Log.e("RegisterError", "Error registering user", e);
                Toast.makeText(MainActivity.this, "Error registering user", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void registerUserOnServer(User user) {
        // Implement your network request logic here to send the user data to your server
    }


    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*");
    }
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}