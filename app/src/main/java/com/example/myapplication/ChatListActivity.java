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

import com.example.myapplication.ROOM_p.Contact;
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
    private List<Contact> contactList = new ArrayList<>();
    private User loggedInUser;
    private String loggedInId;
    private String token;
    private ContactRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        adapter = new ContactRecyclerViewAdapter(this, contactList, loggedInId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView logOutBtn = findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(v -> {
            // navigate to Login Activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

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
                String enteredContactName = input.getText().toString().trim();

                // Execute the AddContactAsyncTask to perform the network operation in the background
                new AddContactAsyncTask().execute(token, enteredContactName);

                // Dismiss the dialog
                dialog.dismiss();
            });

            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        new FetchContactsAsyncTask().execute(token);
    }

    private void setUpUserHeader(String userName, String image, String displayName, String id) {
        loggedInUser = new User(userName, image, displayName, id);
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

    private class FetchContactsAsyncTask extends AsyncTask<String, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(String... params) {
            String token = params[0];
            List<Contact> contactList = new ArrayList<>();

            try {
                URL url = new URL("http://10.0.2.2:5000/api/Chats");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Authorization", "bearer " + token);

                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    String response = Utils.readStream(in);
                    Log.d("ChatList", "response: " + response);

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
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

                        Contact contact = new Contact(loggedInId, displayName, id, profilePic, lastMessageId, lastMessageCreated, lastMessageContent);
                        contactList.add(contact);
                    }
                } else {
                    Log.d("LoginActivity", "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage());
                }

                conn.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return contactList;
        }

        @Override
        protected void onPostExecute(List<Contact> result) {
            if (result != null) {
                contactList.clear();
                contactList.addAll(result);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
