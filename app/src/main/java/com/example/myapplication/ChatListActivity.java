package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplication.ROOM_p.Contact;
import com.example.myapplication.ROOM_p.ContactDao;
import com.example.myapplication.ROOM_p.ContactDatabase;
import com.example.myapplication.ROOM_p.User;
import com.example.myapplication.Utils.Utils;
import com.example.myapplication.recyclerview.ContactRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private ContactDatabase contactDB;
    private ContactDao contactDao;
    List<Contact> contactList = new ArrayList<>();
    User loggedInUSer;
    String loggedInId;
    private String enteredContactName;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_chat_list);

        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("userName");
        String displayName = extras.getString("displayName");
        String image = extras.getString("image");
        String id = extras.getString("UserId");
        token = extras.getString("token");
        loggedInId = extras.getString("loggedInId");


        setUpUserHeader(userName, image, displayName, id);

        RecyclerView recyclerView = findViewById(R.id.rvContactRecyclerView);
        ImageView loggedInImg = findViewById(R.id.SpeakerImg);
        TextView loggedInName = findViewById(R.id.speakerName);

        loggedInName.setText(displayName);
        Log.d("ChatList", "DisplayNAme: " + displayName);
//        loggedInImg.setImageResource();

        setUpUserHeader(userName, image,displayName, id);

        contactDB = Room.databaseBuilder(getApplicationContext(), ContactDatabase.class, "ContactDB").allowMainThreadQueries().build();
        contactDao = contactDB.contactDao();
        contactList = contactDao.getContactsByUserId(loggedInId);

        ContactRecyclerViewAdapter adapter = new ContactRecyclerViewAdapter(this, contactList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView logOutBtn = findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(v -> {
            // navigate to Login Activity
            startActivity(new Intent(this,
                    com.example.myapplication.LoginActivity.class));
            finish();
        });
        //addContactBtn
        ImageView addContactBtn = findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatListActivity.this);
            alertDialogBuilder.setTitle("Add contact");

            // Create the input field
            final EditText input = new EditText(ChatListActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            alertDialogBuilder.setView(input);

            alertDialogBuilder.setPositiveButton("Add", (dialog, which) -> {
                // Retrieve the entered contact name
                enteredContactName = input.getText().toString().trim();

                // Execute the AddContactAsyncTask to perform the network operation in the background
                new AddContactAsyncTask().execute(token, enteredContactName);

                // Dismiss the dialog
                dialog.dismiss();
            });

            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    private void setUpUserHeader(String userName, String image,String displayName,String id) {
        this.loggedInUSer =  new User(userName, image, displayName, id);
    }

    private class AddContactAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String token = params[0];
            String name = params[1];

            try {
                URL url = new URL("http://10.0.2.2:5000/api/Chats" + name);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "bearer " + token);
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", name);

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    InputStream in = conn.getInputStream();
                    String response = Utils.readStream(in);
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("ChatListActivity", "response from add contact: " + jsonArray);
                }

                conn.disconnect();

                return responseCode;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode == 200) {
                // Contact added successfully
                Toast.makeText(ChatListActivity.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
            } else if (responseCode == 404) {
                // User does not exist
                Toast.makeText(ChatListActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
            } else {
                // Other error occurred
                Toast.makeText(ChatListActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
