package com.example.myapplication;

import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.ChatDB;
import com.example.myapplication.DB.MessageDB;
import com.example.myapplication.Dao.ChatsDao;
import com.example.myapplication.Dao.MessageDao;
import com.example.myapplication.Entites.Chat;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.Entites.MessageItem;
import com.example.myapplication.Utils.Utils;
import com.example.myapplication.ViewModels.BaseUrlInterceptor;
import com.example.myapplication.ViewModels.ContactViewModel;
import com.example.myapplication.ViewModels.MessageViewModel;
import com.example.myapplication.api.ChatAPI;
import com.example.myapplication.recyclerview.MessageRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private MutableLiveData<List<Message>> messageList;
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

    private MessageViewModel messageViewModel;

    private List<Message> messages = new ArrayList<>();

    private String currentUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String savedBaseUrl = prefs.getString("baseUrl", "http://10.0.2.2:5000/api/");
        BaseUrlInterceptor.getInstance().setBaseUrl(savedBaseUrl);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView contactName = findViewById(R.id.currentContactName);
        ImageView contactPic = findViewById(R.id.currentContactImg);



        chatDB = ChatDB.getInstance(this);
        chatsDao = chatDB.chatDao();

        messageDB = MessageDB.getInstance(this);
        messageDao = messageDB.messageDao();

        //chatAPI = new ChatAPI(chatsDao, messageList, messageDao);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUserName = extras.getString("currentUserName");
//
            contactId = extras.getInt("contactId");
            chatID = extras.getString("serverChatID");
            currentChat = new Chat(chatID);
            new Thread(() -> {
                chatsDao.insert(currentChat);
            }).start();
            contactName.setText(extras.getString("displayName"));
            contactPic.setImageBitmap(Utils.StringToBitMap(extras.getString("profilePic")));

            token = MyApplication.getToken();
        }

        recyclerView = findViewById(R.id.rvMessageRecyclerView);





        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.initializeData(chatID, currentUserName);
        Log.d("ChatActivity", "Loading messages...");

        messageViewModel.getAllMessages().observe(this, newMessageList -> {
            if (newMessageList != null) {
                messages = newMessageList;
                messageAdapter = new MessageRecyclerViewAdapter(ChatActivity.this, messages, currentUserName);
                recyclerView.setAdapter(messageAdapter);
            }
        });
       //loadChatMessages();

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
                messageViewModel.add(chatID, messageInput.getText().toString());
                /*new Thread(() -> {
                    chatAPI.addMessage(chatID, messageInput.getText().toString());
                }).start();*/
                messageInput.setText("");
            }
        });

        ImageView backBtn = findViewById(R.id.backToContactsBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageRecyclerViewAdapter(this, messages, currentUserName);
        recyclerView.setAdapter(messageAdapter);


    }
}