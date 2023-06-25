package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.ContactsDB;
import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.State.LoggedUser;
import com.example.myapplication.Utils.Utils;
import com.example.myapplication.api.ContactAPI;
import com.example.myapplication.recyclerview.ContactRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private MutableLiveData<List<Contact>> contactList;
    private List<Contact> contacts;
    private ContactRecyclerViewAdapter adapter;

    private ContactDao contactDao;
    private ContactsDB contactDB;
    private ContactAPI contactAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        contactList = new MutableLiveData<>();

        contactDB = ContactsDB.getInstance(this);
        contactDao = contactDB.contactDao();
        contactAPI = new ContactAPI(contactDao, contactList);
        contactAPI.getAllContacts(MyApplication.getToken());

        ImageView imgView = findViewById(R.id.SpeakerImg);
        TextView nameView = findViewById(R.id.speakerName);
        nameView.setText(LoggedUser.getDisplayName());
        imgView.setImageBitmap(Utils.StringToBitMap(LoggedUser.getProfilePic()));

        RecyclerView recyclerView = findViewById(R.id.rvContactRecyclerView);

        adapter = new ContactRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList.observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> updatedContactList) {
                adapter.setContactList(updatedContactList);
                adapter.notifyDataSetChanged();
            }
        });

//
//        new Thread(()-> {
//            contacts =  contactDao.getAllContacts();
//            Log.d("ChatListActivity","ContactList from local DB: "+contacts);
//
//
//        }).start();
//
//        adapter = new ContactRecyclerViewAdapter(this, contacts);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView logOutBtn = findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(v -> {
            // navigate to Login Activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
//
//        ImageView addContactBtn = findViewById(R.id.addContactBtn);
//        addContactBtn.setOnClickListener(v -> {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatListActivity.this);
//            alertDialogBuilder.setTitle("Add contact");
//
//            // Create the input field
//            final EditText input = new EditText(ChatListActivity.this);
//            input.setInputType(InputType.TYPE_CLASS_TEXT);
//            alertDialogBuilder.setView(input);
//
//            alertDialogBuilder.setPositiveButton("Add", (dialog, which) -> {
//                // Retrieve the entered contact name
//                String enteredContactName = input.getText().toString().trim();
//
//                // Execute the AddContactAsyncTask to perform the network operation in the background
//                new AddContactAsyncTask().execute(enteredContactName);
//
//                // Dismiss the dialog
//                dialog.dismiss();
//            });
//
//            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        });

//        new FetchContactsAsyncTask().execute();
    }

//    private class AddContactAsyncTask extends AsyncTask<String, Void, Integer> {
//
//        @Override
//        protected Integer doInBackground(String... params) {
//            String token = params[0];
//            String name = params[1];
//
//            try {
//                URL url = new URL("http://10.0.2.2:5000/api/Chats" + name);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                conn.setRequestProperty("Accept", "application/json");
//                conn.setRequestProperty("Authorization", "bearer " + token);
//                conn.setDoOutput(true);
//                conn.setDoInput(true);
//
//                JSONObject jsonParam = new JSONObject();
//                jsonParam.put("username", name);
//
//                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                os.writeBytes(jsonParam.toString());
//                os.flush();
//                os.close();
//
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == 200) {
//                    InputStream in = conn.getInputStream();
//                    String response = Utils.readStream(in);
//                    JSONArray jsonArray = new JSONArray(response);
//                    Log.d("ChatListActivity", "response from add contact: " + jsonArray);
//                }
//
//                conn.disconnect();
//
//                return responseCode;
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//
//            return -1;
//        }

//        @Override
//        protected void onPostExecute(Integer responseCode) {
//            if (responseCode == 200) {
//                // Contact added successfully
//                Toast.makeText(ChatListActivity.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
//            } else if (responseCode == 404) {
//                // User does not exist
//                Toast.makeText(ChatListActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
//            } else {
//                // Other error occurred
//                Toast.makeText(ChatListActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
//            }
//        }
    //}

//    private class FetchContactsAsyncTask extends AsyncTask<String, Void, List<Contact>> {
//
//        @Override
//        protected List<Contact> doInBackground(String... params) {
//            String token = params[0];
//            List<Contact> contactList = new ArrayList<>();
//
//            try {
//                URL url = new URL("http://10.0.2.2:5000/api/Chats");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                conn.setRequestProperty("Authorization", "bearer " + token);
//
//                if (conn.getResponseCode() == 200) {
////                    InputStream in = conn.getInputStream();
////                    String response = Utils.readStream(in);
////                    Log.d("ChatList", "response: " + response);
////
////                    JSONArray jsonArray = new JSONArray(response);
////                    for (int i = 0; i < jsonArray.length(); i++) {
////                        JSONObject jsonObject = jsonArray.getJSONObject(i);
////                        String id = jsonObject.getString("id");
////                        JSONObject user = jsonObject.getJSONObject("user");
////                        String username = user.getString("username");
////                        String displayName = user.getString("displayName");
////                        String profilePic = user.getString("profilePic");
////                        JSONObject lastMessage = jsonObject.getJSONObject("lastMessage");
////                        String lastMessageId = lastMessage.getString("id");
////                        String lastMessageCreated = lastMessage.getString("created");
////                        String lastMessageContent = lastMessage.getString("content");
////                        //int id, String contactName, String lastMsg, String lastMsgDate, String contactPic
////                        Contact contact = new Contact(id, displayName, id, profilePic, lastMessageId, lastMessageCreated, lastMessageContent);
////                        contactList.add(contact);
////                    }
//                } else {
//                    Log.d("LoginActivity", "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage());
//                }
//
//                conn.disconnect();
//            } catch (IOException e) {
////                e.printStackTrace();
//            }
//
//            return contactList;
//        }
//
//        @Override
//        protected void onPostExecute(List<Contact> result) {
//            if (result != null) {
//                contactList.clear();
//                contactList.addAll(result);
//                adapter.notifyDataSetChanged();
//            }
//        }
//    }
}
