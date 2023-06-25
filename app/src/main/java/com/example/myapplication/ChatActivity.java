package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.recyclerview.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private List<Message> messageList = new ArrayList<>();
    private String loggedInUserId;
    private String contactId;
    private String token;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.rvMessageRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUserId = extras.getString("loggedInUserId");
            contactId = extras.getString("contactId");
            Log.d("ChatActivity", "Chat "+contactId);

            token = MyApplication.getToken();
        }

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        loadChatMessages();

        ImageView backBtn = findViewById(R.id.backToContactsBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private void loadChatMessages() {
        Log.d("ChatActivity", "Loading messages...");
        Thread thread = new Thread() {
//            public void run() {
//                try {
//                    String urlStr = "http://10.0.2.2:5000/api/Chats/" + contactId + "/Messages";
//                    Log.d("ChatActivity", "contact id: " + contactId);
//
//                    URL url = new URL(urlStr);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                    conn.setRequestProperty("Authorization", "bearer " + token);
//
//                    if (conn.getResponseCode() == 200) {
//                        InputStream in = conn.getInputStream();
//                        String response = Utils.readStream(in);
//                        Log.d("ChatActivity", "response: " + response);
//
//                        JSONArray jsonArray = new JSONArray(response);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String id = jsonObject.getString("id");
//                            String content = jsonObject.getString("content");
//                            String created = jsonObject.getString("created");
//                            boolean isSentByMe = loggedInUserId.equals(jsonObject.getString("senderId"));
//
//                            Message message = new Message(id, content, created, isSentByMe);
//                            messageList.add(message);
//                        }
//
//                        runOnUiThread(() -> {
//                            messageAdapter.notifyDataSetChanged();
//                        });
//                    } else {
//                        Log.d("ChatActivity", "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage());
//                    }
//
////                    conn.disconnect();
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        };
        thread.start();
    }
}
