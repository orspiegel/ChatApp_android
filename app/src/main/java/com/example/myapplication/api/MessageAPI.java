package com.example.myapplication.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Dao.ChatAndMessagesDao;
import com.example.myapplication.Dao.ChatsDao;
import com.example.myapplication.Dao.MessageDao;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Objects.AddMessageRequest;
import com.example.myapplication.Objects.ConversationPageResponse;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.Objects.MessageResponse;
import com.example.myapplication.Objects.getAllMessagesResponse;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MessageAPI {

    private Retrofit retrofit;
    // remote
    private WebServiceAPI webServiceAPI;
    // local
    private MessageDao messageDao;

    private MutableLiveData<List<Message>> messages;


    public MessageAPI(MessageDao messageDao, MutableLiveData<List<Message>> messages){
        String url = MyApplication.context.getString(R.string.baseUrl);
        retrofit = (new Retrofit.Builder().baseUrl(url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.messages = messages;
        this.messageDao = messageDao;
    }

    public void getChatContent(String chatID) {
        Call<List<getAllMessagesResponse>> call = webServiceAPI.getContactMessages("bearer " + MyApplication.getToken(), chatID);
        call.enqueue(new Callback<List<getAllMessagesResponse>>() {
            @Override
            public void onResponse(Call<List<getAllMessagesResponse>> call, Response<List<getAllMessagesResponse>> response) {
                Log.d("MessageResponse", String.valueOf(response.code()));
                Executors.newSingleThreadExecutor().execute(new Runnable() {

                    public void run() {
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
                        if (parsedMessages.size() > 0) {
                            messageDao.deleteChatMessages(chatID);
                            for (MessageItem mI : parsedMessages) {
                                Message chatMessage = new Message(mI, chatID);
                                messageDao.insert(chatMessage);
                            }

                            for (Message message : messageDao.getChatMessages(chatID)) {
                                Log.d("insertion complete", message.getChat_id() + " " +
                                        message.getContent() + " " + message.getCreated() + " " +
                                        message.getSenderUserName() + " " + message.getMsgID());

                            }

                            messages.postValue(messageDao.getChatMessages(chatID));
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<getAllMessagesResponse>> call, Throwable t) {
                Log.d("Chat", "Error! "+t);
            }
        });
    }

    public void addMessage(String chatID, String msgContent) {
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

                        Message newMessage = new Message(lastMessage, chatID);
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
            }
        });
    }
}