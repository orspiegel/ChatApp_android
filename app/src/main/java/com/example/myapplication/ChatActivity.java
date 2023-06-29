package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.ChatDB;
import com.example.myapplication.DB.MessageDB;
import com.example.myapplication.Dao.ChatsDao;
import com.example.myapplication.Dao.MessageDao;
import com.example.myapplication.Entites.Chat;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.Utils.Utils;
import com.example.myapplication.api.ChatAPI;
import com.example.myapplication.recyclerview.MessageRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private MutableLiveData<List<MessageItem>> messageList;
    private String loggedInUserId;
    private String chatID;
    private String token;
    private RecyclerView recyclerView;
    private MessageRecyclerViewAdapter messageAdapter;

    private MessageDB messageDB;

    private MessageDao messageDao;

    private ChatsDao chatsDao;
    private ChatDB chatDB;

    private ChatAPI chatAPI;

    private int contactId;

    private Chat currentChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.rvMessageRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView contactName = findViewById(R.id.currentContactName);
        ImageView contactPic = findViewById(R.id.currentContactImg);

        chatDB = ChatDB.getInstance(this);
        chatsDao = chatDB.chatDao();

        messageDB = MessageDB.getInstance(this);
        messageDao = messageDB.messageDao();

        chatAPI = new ChatAPI(chatsDao, messageList, messageDao);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUserId = extras.getString("loggedInUserId");
            contactId = extras.getInt("contactId");
            //Log.d("ChatActivity", "Chat "+contactId);
            chatID = extras.getString("serverChatID");
            currentChat = new Chat(chatID);
            new Thread(() -> {
                chatsDao.insert(currentChat);
            }).start();
            contactName.setText(extras.getString("contactName"));
            contactPic.setImageBitmap(Utils.StringToBitMap(extras.getString("contactPic")));

            token = MyApplication.getToken();
        }

        messageAdapter = new MessageRecyclerViewAdapter(this);
        recyclerView.setAdapter(messageAdapter);
        loadChatMessages();

        EditText messageInput = findViewById(R.id.message);
        ImageView sendButton = findViewById(R.id.button_gchat_send);

        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean btnTrigger = (!(messageInput.getText().toString().trim().isEmpty()));
                sendButton.setEnabled(btnTrigger);
                if (btnTrigger) {
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendButton.setOnClickListener(v -> {
            //sendMessage(messageInput.getText().toString());
            if (!messageInput.getText().toString().isEmpty()) {
                new Thread(() -> {
                    chatAPI.addMessage(chatID, messageInput.getText().toString());
                }).start();
                messageInput.getText().clear();
            }
        });

        ImageView backBtn = findViewById(R.id.backToContactsBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private void loadChatMessages() {
        Log.d("ChatActivity", "Loading messages...");

        new Thread(() -> {
            chatAPI.getChatContent(chatID);
        }).start();
    }

    public void onMessageUpdate() {

        //List<MessageItem> messages = chatsDao.getAllMessages(chatID);
        //List<MessageItem> messageList = chatsDao.getAllMessages(chatID);
        //Log.d("ChatListActivity", "contacts on DB:"+messageList);
        //Log.d("ChatListActivity", "contacts size on DB:"+messageList.size());


        runOnUiThread(()-> {
            //messageAdapter.setMessageList(messageList);
            //messageAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
        });
    }
}
