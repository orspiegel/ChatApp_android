package com.example.myapplication.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.example.myapplication.Dao.ChatAndMessagesDao;
import com.example.myapplication.Dao.ChatsDao;
import com.example.myapplication.Dao.MessageDao;
import com.example.myapplication.Entites.ChatAndMessages;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Objects.AddMessageRequest;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.Objects.MessageResponse;
import com.example.myapplication.R;
import com.example.myapplication.State.LoggedUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {

    private Retrofit retrofit;
    // remote
    private WebServiceAPI webServiceAPI;
    // local
    private ChatsDao chatDao;

    private MessageDao messageDao;

    private ChatAndMessagesDao chatAndMessagesDao;
    // local
    private MutableLiveData<List<MessageItem>> messages;


    public ChatAPI(ChatsDao chatDao, MutableLiveData<List<MessageItem>> messages, MessageDao messageDao){
        String url = MyApplication.context.getString(R.string.baseUrl);
        retrofit = (new Retrofit.Builder().baseUrl(url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.chatDao = chatDao;
        this.messages = messages;
        this.messageDao = messageDao;
    }

    public void getChatContent(String chatID) {
        Call<List<MessageResponse>> call = webServiceAPI.getContactMessages("bearer " + MyApplication.getToken(), chatID);
        call.enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                Log.d("Chat", String.valueOf(response.code()));
                new Thread(() -> {
                    if (response.body() != null) {
                        ChatAndMessages chatAndMessages = chatAndMessagesDao.getChatWithMessages(chatID);
                        List<MessageItem> parsedMessages = new ArrayList<>();
                        Log.d("Chat", "Chat response: " + response.body());
                        List<MessageResponse> chatMessages = new ArrayList<>(response.body());
                        //chatDao.updateMessageList(chatID, chatMessages);*/
                        for (MessageResponse mR : response.body()) {
                            MessageItem messageItem = new MessageItem();
                            messageItem.setContent(mR.getContent());
                            messageItem.setCreated(mR.getCreated());
                            messageItem.setMsgID(mR.getId());
                            messageItem.setSender(mR.getSender());
                            Log.d("RESULT", messageItem.getContent() + messageItem.getMsgID() + messageItem.getCreated() + messageItem.getSender());
                            parsedMessages.add(messageItem);
                        }
                        //chatDao.updateMessageList(chatID, parsedMessages);
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Log.d("Chat", "Error! "+t);
            }
        });
    }

    public void addMessage(String chatID, String msgContent) {
        Log.d("TO CREATE", chatID + " " + msgContent);
        AddMessageRequest addMessageRequest = new AddMessageRequest(msgContent);
        String token = "bearer " + MyApplication.getToken();
        Call<Void> call = webServiceAPI.addMessage(chatID, addMessageRequest, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Chat", String.valueOf(response.code()));
                new Thread(() -> {
                    Log.d("Chat", "RESPONDED");


                    Message newMessage = new Message(chatID, msgContent, LoggedUser.getUserName());
                    long newMsgID = messageDao.insert(newMessage);

                    if(newMsgID != -1) {
                        // Successful insertion
                        System.out.println("Message saved successfully: " + newMessage.getContent());
                    } else {
                        // Failure in insertion
                        System.out.println("Failed to save message");
                    }

                    for (Message msg : messageDao.getAllMessages()) {
                        Log.d("Current Messages: ", msg.getSenderUserName() + msg.getContent() + msg.getTimeStamp());
                    }

                }).start();
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Chat", "Error! "+t);
            }
        });
    }
}