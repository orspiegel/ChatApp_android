package com.example.myapplication.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Repository.MessageRepository;

import java.io.Serializable;
import java.util.List;

public class MessageViewModel extends ViewModel implements Serializable {
    private MessageRepository repository;
    private LiveData<List<Message>> allMessages;

    private boolean inChat = false;

    public MessageViewModel() {
    }

    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public void add(String chatID, String message) {
        repository.add(chatID, message);
    }

    public void initializeData(String chatID, String currentUserName) {
        if (!inChat) {
            inChat = true;
            this.repository = new MessageRepository(MyApplication.context, chatID, currentUserName);
            this.allMessages = repository.getAll();
        }
    }
}
