package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.Entites.User;
import com.example.myapplication.ViewModels.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView avatar;
    private EditText username;
    private EditText nickname;
    private EditText password;
    private EditText confirmPassword;
    private Bitmap selectedImage;
    private UserViewModel userViewModel;

//    private com.example.myapplication.UserDatabaseHelper dbHelper;
    // Create an ActivityResultLauncher
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

//        UserAPI userAPI = new UserAPI();
//        userAPI.get();

        // TODO: contacts
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        UserAdapter adapter = new UserAdapter();
//        recyclerView.setAdapter(adapter);

        // TEST - Or
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                // update recyclerView
            }
        });
        // TEST end - OR
        Log.d("tag", "start point");
        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        Button registerButton = findViewById(R.id.register_button2);

//        dbHelper = new com.example.myapplication.UserDatabaseHelper(this);

        avatar.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mGetContent.launch(Intent.createChooser(intent, "Select Picture"));
        });
        Log.d("tag", "avatar");

        registerButton.setOnClickListener(v -> {
                try {
                    Log.d("MainActivity", "start register");
                    String usernameValue = username.getText().toString();
                    String displayNameValue = nickname.getText().toString();
                    String passwordValue = password.getText().toString();
                    String confirmPasswordValue = confirmPassword.getText().toString();

                    // Check for whitespace in the username
                    if (usernameValue.contains(" ")) {
                        Toast.makeText(MainActivity.this, "Please enter a valid username - no space, tab or new-line characters allowed.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Check if password is valid
                    if (!isPasswordValid(passwordValue)) {
                        Toast.makeText(MainActivity.this, "Password must be at least 8 characters long and contain letters and numbers.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Check if passwords match
                    if (!passwordValue.equals(confirmPasswordValue)) {
                       Toast.makeText(MainActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Check display name
                    if (displayNameValue.trim().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please enter a valid display name - must contain at least one letter or number.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (selectedImage == null) {
                       Toast.makeText(MainActivity.this, "Please select an image.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Continue with your code
                    Log.d("tag", "encode picture");
                    String profilePicValue = encodeImage(selectedImage);  // Assuming you have converted image to Base64 string
                    Log.d("RegisterDebug", "Values are: " + usernameValue + ", " + displayNameValue + ", " + passwordValue);

//                    URL url = new URL("http://10.0.2.2:5000/api/Users");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                    conn.setRequestProperty("Accept", "application/json");
//                    conn.setDoOutput(true);
//                    conn.setDoInput(true);
//
//                    JSONObject jsonParam = new JSONObject();
//                    jsonParam.put("username", usernameValue);
//                    jsonParam.put("displayName", displayNameValue);
//                    jsonParam.put("password", passwordValue);
//                    jsonParam.put("profilePic", profilePicValue);
//                    Log.d("RegisterDebug", "JSON object: " + jsonParam.toString());
//                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                    os.writeBytes(jsonParam.toString());
//                    os.flush();
//                    os.close();
//
//                    System.out.println("Response Code: " + conn.getResponseCode());
//                    System.out.println("Response Message: " + conn.getResponseMessage());

                    // Save user to SQLite database
                    ArrayList<String> contactList = new ArrayList<>();
//                    long newRowId = dbHelper.insertData(usernameValue, passwordValue, displayNameValue, profilePicValue, contactList, false);
//                    if (newRowId == -1) {
//                        System.out.println("Error occurred while inserting user data into SQLite database.");
//                    } else {
//                        System.out.println("User data inserted into SQLite database with row ID: " + newRowId);
//                    }
//                    Cursor res = dbHelper.getAllData();
//                    while (res.moveToNext()) {
//                        String userData = "Row ID: " + res.getString(0) +
//                                " Username: " + res.getString(1) +
//                                " Password: " + res.getString(2) +
//                                " isAuthenticated: " + res.getString(3) +
//                                " Contact List: " + res.getString(4);
//                        Log.d("DBData", userData);
//                    }
//                    Intent intent = new Intent(MainActivity.this, com.example.myapplication.LoginActivity.class);
//                    startActivity(intent);
//                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        });

        Button loginButton = findViewById(R.id.log_in_button);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.myapplication.LoginActivity.class);
            startActivity(intent);
        });
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