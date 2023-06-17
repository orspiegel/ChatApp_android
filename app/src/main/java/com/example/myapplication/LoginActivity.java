package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.ROOM_p.Contact;
import com.example.myapplication.ROOM_p.ContactDao;
import com.example.myapplication.ROOM_p.ContactDatabase;
import com.example.myapplication.ROOM_p.User;
import com.example.myapplication.ROOM_p.UserDao;
import com.example.myapplication.ROOM_p.UserDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private UserDatabase userDB;
    private UserDao userDao;
    private String loggedInId;

    private ContactDatabase contactDB;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login_button);

        // DB operations
        userDB = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "UserDB").allowMainThreadQueries().build();
        userDao = userDB.userDao();

        contactDB = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class, "ContactDB").allowMainThreadQueries().build();
        contactDao = contactDB.contactDao();


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
                        // Store token in shared preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("myapplication", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.apply();

                        UserDatabaseHelper dbHelper = new UserDatabaseHelper(LoginActivity.this);
                        dbHelper.updateUserAuthentication(usernameValue, true);
                        // Navigate to other activity
                        Intent intent = new Intent(LoginActivity.this,
                                com.example.myapplication.ChatListActivity.class);

                        User loggedIn = getUserData(usernameValue, token);

                        userDao.insert(loggedIn);
                        ArrayList<Contact> contacts = getContactList(token);
                        for (int i = 0; i < contacts.size(); i++) {
                            contactDao.insert(contacts.get(i));
                        }
                        intent.putExtra("userName", loggedIn.getUserName());
                        intent.putExtra("displayName", loggedIn.getDisplayName());
                        intent.putExtra("image", loggedIn.getImage());
                        intent.putExtra("UserId", loggedIn.getUserId());
                        intent.putExtra("token", token);
                        intent.putExtra("loggedInId", this.loggedInId);

                        Log.d("LoginActivity", "data passed to chats screen: " + loggedIn.getUserName() + loggedIn.getDisplayName()+ loggedIn.getImage()+ loggedIn.getUserId());

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
            startActivity(new Intent(LoginActivity.this,
                    com.example.myapplication.MainActivity.class));
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
    public User getUserData(String userName, String token) throws IOException {
        Log.d("LoginActivity", "got into Chat activity");
        URL url = new URL("http://10.0.2.2:5000/api/Users/" + userName);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Authorization","bearer "+ token);
        Log.d("LoginActivity", "Res code:" + conn.getResponseCode());
        User user = null;
        JSONObject responseJson = null;

        if(conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            String response = readStream(in);
            try {
                responseJson = new JSONObject(response);
                conn.disconnect();

                String id = responseJson.getString("_id");
                this.loggedInId = id;
                String username = responseJson.getString("username");
                String displayName = responseJson.getString("displayName");
                String profilePic = responseJson.getString("profilePic");
                user = new User(username, profilePic, displayName, id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("LoginActivity", "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage());
        }
        conn.disconnect();
        return user;
    }

    public ArrayList<Contact> getContactList(String token) throws IOException {
        ArrayList<Contact> contactList = new ArrayList<>();
        URL url = new URL("http://10.0.2.2:5000/api/Chats");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Authorization","bearer "+ token);
        if(conn.getResponseCode() == 200) {
            InputStream in = conn.getInputStream();
            String response = readStream(in);
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Contact c;
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    JSONObject user = jsonObject.getJSONObject("user");
                    String username = user.getString("username");
                    String displayName = user.getString("displayName");
                    String profilePic = user.getString("profilePic");
                    JSONObject lastMessage = jsonObject.getJSONObject("lastMessage");
                    String lastMessageId = lastMessage.getString("id");
                    String lastMessageCreated = lastMessage.getString("created");
                    String lastMessageContent = lastMessage.getString("content");
                    c = new Contact(this.loggedInId,displayName, id, profilePic, lastMessageId, lastMessageCreated, lastMessageContent);
                    // Check if the contact already exists in the database
                    if (contactDao.get(id) == null) {
                        // Contact doesn't exist, insert it into the database
                        contactList.add(c);
                    }
                }
                conn.disconnect();
                return contactList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("LoginActivity", "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage());
        }
        conn.disconnect();
        return null;
    }

}