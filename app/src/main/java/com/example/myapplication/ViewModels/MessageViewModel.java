package com.example.myapplication.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Repository.ContactRepository;
import com.example.myapplication.Repository.MessageRepository;
import com.example.myapplication.State.CurrentContact;

import java.io.Serializable;
import java.util.List;

public class MessageViewModel extends ViewModel implements Serializable {
    private MessageRepository repository;
    private LiveData<List<Message>> allMessages;

//    private boolean inChat = false;

    public MessageViewModel() {
        repository = new MessageRepository(MyApplication.context, CurrentContact.getId());

    }

    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public void add(String chatID, String message) {
        repository.add(chatID, message);
    }

    public void initializeData(String chatID) {
        if (!CurrentContact.isClicked) {
            CurrentContact.isClicked = true;
            this.repository = new MessageRepository(MyApplication.context, chatID);
            this.allMessages = repository.getAll();
        }
    }
}
