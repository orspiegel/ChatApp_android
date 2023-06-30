package com.example.myapplication.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.DB.ChatDB;
import com.example.myapplication.DB.MessageDB;
import com.example.myapplication.Dao.ChatsDao;
import com.example.myapplication.Dao.MessageDao;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.api.ChatAPI;

import java.util.LinkedList;
import java.util.List;

public class MessageRepository {
    private MessageDao messageDao;
    private MessageListData allMessages;
    private MessageDB messageDB;

    private ChatDB chatDB;

    private ChatsDao chatsDao;
    private ChatAPI chatAPI;

    private String chatID;

    public MessageRepository(Context context, String chatID)  {
        chatDB = chatDB.getInstance(context);
        chatsDao = chatDB.chatDao();
        messageDB = messageDB.getInstance(context);
        messageDao = messageDB.messageDao();
        allMessages = new MessageListData();
        chatAPI = new ChatAPI(chatsDao, allMessages, messageDao);
        this.chatID = chatID;
    }

class MessageListData extends MutableLiveData<List<Message>> {
    public MessageListData() {
        super();
        // local database
        setValue(new LinkedList<>());
    }
    @Override
    protected void onActive() {
        super.onActive();
        new Thread(() -> {
            allMessages.postValue(messageDao.getChatMessages(chatID));
            chatAPI.getChatContent(chatID);
        }).start();
    }
}
    public LiveData<List<Message>> getAll() {
        return allMessages;
    }
    /*public void update(final Contact contact) {
        // update contact on local db
        contactDao.update(contact);
        // update contact on remote db
        chatAPI.update(contact);
    }
    public void delete(final Message message) {
        // delete contact from local db
        messageDao.delete(message);
        // delete contact from remote db
        //chatAPI.delete(message);
    }*/
    public void add(String chatId, String msg) {
        chatAPI.addMessage(chatId, msg);
    }
}
