package com.example.myapplication.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Insert;


import com.example.myapplication.Dao.ChatAndMessagesDao;
import com.example.myapplication.Dao.ChatsDao;
import com.example.myapplication.Dao.MessageDao;
import com.example.myapplication.Entites.ChatAndMessages;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Objects.AddMessageRequest;
import com.example.myapplication.Objects.ConversationPageResponse;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.Objects.MessageResponse;
import com.example.myapplication.Objects.User;
import com.example.myapplication.Objects.getAllMessagesResponse;
import com.example.myapplication.R;
import com.example.myapplication.State.LoggedUser;
import com.example.myapplication.ViewModels.BaseUrlInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {

    private  String currentUserName;
    private Retrofit retrofit;
    // remote
    private WebServiceAPI webServiceAPI;
    // local
    private ChatsDao chatDao;

    private MessageDao messageDao;

    private ChatAndMessagesDao chatAndMessagesDao;
    // local
    private MutableLiveData<List<Message>> messages;


    public ChatAPI(ChatsDao chatDao, MutableLiveData<List<Message>> messages, MessageDao messageDao, String currentUserName){

        SharedPreferences prefs = MyApplication.context.getSharedPreferences("AppSettings", MODE_PRIVATE);
        String savedBaseUrl = prefs.getString("baseUrl", "http://10.0.2.2:5000/api/");
        BaseUrlInterceptor.getInstance().setBaseUrl(savedBaseUrl);

        String url = MyApplication.context.getString(R.string.baseUrl);
        retrofit = (new Retrofit.Builder().baseUrl(url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.chatDao = chatDao;
        this.messages = messages;
        this.messageDao = messageDao;
        this.currentUserName = currentUserName;
        Log.d("Please", this.currentUserName);
    }

    public void getChatContent(String chatID) {
        Call<List<getAllMessagesResponse>> call = webServiceAPI.getContactMessages("bearer " + MyApplication.getToken(), chatID);
        call.enqueue(new Callback<List<getAllMessagesResponse>>() {
            @Override
            public void onResponse(Call<List<getAllMessagesResponse>> call, Response<List<getAllMessagesResponse>> response) {
                Log.d("MessageResponse", String.valueOf(response.code()));
                Executors.newSingleThreadExecutor().execute(new Runnable() {

                    public void run() {

                        if (response.code() == 404) {
                            Toast.makeText(MyApplication.context, "Chat not found.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<MessageItem> parsedMessages = new ArrayList<>();
                        for (getAllMessagesResponse mR : response.body()) {
                            Log.i("response", "Res: " + mR.getId());
                            Log.i("response", "Res: " + mR.getContent());
                            Log.i("response", "Res: " + mR.getCreated());
                            Log.i("response", "Res: " + mR.getSender().getUserName());
                            MessageItem parsed = new MessageItem(mR.getId(),
                                    mR.getCreated(),
                                    mR.getSender().getUserName(),
                                    mR.getContent());
                            parsedMessages.add(parsed);
                        }

                            for (MessageItem mI : parsedMessages) {
                                Message chatMessage = new Message(mI, chatID);
                                messageDao.insert(chatMessage);
                            }

                            messages.postValue(messageDao.getChatMessages(chatID));

                    }
                });
            }

            @Override
            public void onFailure(Call<List<getAllMessagesResponse>> call, Throwable t) {
                Log.d("Chat", "Error! "+t);
                Toast.makeText(MyApplication.context, "Could not Load chat messages", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void addMessage(String chatID, String msgContent) {

        //TODO: ExpectedResponse - Chat Object - chatID, Array of Two User ID's and Array of Messages
        // Current failure caused by "Error! com.google.gson.JsonSyntaxException: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 45 path $.users[0]"
        Log.d("TO CREATE", chatID + " " + msgContent);
        AddMessageRequest addMessageRequest = new AddMessageRequest(msgContent);
        Log.d("Contact api add","body " + addMessageRequest );
        String token = MyApplication.getToken();
        Call<ConversationPageResponse> call = webServiceAPI.addMessage(chatID, addMessageRequest, "bearer " + token);
        call.enqueue(new Callback<ConversationPageResponse>() {
            @Override
            public void onResponse(Call<ConversationPageResponse> call, Response<ConversationPageResponse> response) {
                Log.d("Chat", String.valueOf(response.code()));
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                            public void run() {
                        Log.d("LastMessage", response.body().getId() + " " + response.body().getUsers().get(0)
                                + " " + response.body().getUsers().get(1) + " " + response.body().getMessages().toString());
                        MessageResponse lastServerMessage = response.body().getMessages()
                                .get(response.body().getMessages().size() - 1);
                        Log.d("lastMessage", lastServerMessage.get_id() + " " + lastServerMessage.getContent() + " " +
                                lastServerMessage.getCreated() + " " + lastServerMessage.getSender());
                        MessageItem lastMessage = new MessageItem(lastServerMessage.getId(),
                                lastServerMessage.getCreated(), lastServerMessage.getSender(),
                                lastServerMessage.getContent());

                        Message newMessage = new Message(lastMessage, chatID, currentUserName);
                        long newMsgID = messageDao.insert(newMessage);

                        if (newMsgID != -1) {
                            // Successful insertion
                            System.out.println("Message saved successfully: " + newMessage.getContent());
                        } else {
                            // Failure in insertion
                            System.out.println("Failed to save message");
                        }

                        for (Message msg : messageDao.getAllMessages()) {
                            Log.d("Current Messages: ", msg.getSenderUserName() + msg.getContent() + msg.getCreated());
                        }

                        List<Message> newMessageList = messages.getValue();
                        newMessageList.add(newMessage);
                        messages.postValue(newMessageList);
                    }

                });
            }


            @Override
            public void onFailure(Call<ConversationPageResponse> call, Throwable t) {
                Log.d("Chat", "Error! "+t);
                Toast.makeText(MyApplication.context, "Target User/Chat not found.", Toast.LENGTH_LONG).show();
            }
        });
    }
}