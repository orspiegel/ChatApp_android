package com.example.myapplication.Entites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myapplication.Objects.MessageItem;

import java.util.List;
/*
Usage: GET baseURL/api/Chats/chatWithID/Messages
* */

@Entity(tableName = "chats_table")
public class Chats {
    @PrimaryKey
    int id;
    String chatWithID;
    List<MessageItem> messagesList;

    public Chats() {}

    public Chats(String chatWithID, List<MessageItem> messagesList) {
        this.chatWithID = chatWithID;
        this.messagesList = messagesList;
    }

    public String getChatWithID() {
        return chatWithID;
    }

    public void setChatWithID(String chatWithID) {
        this.chatWithID = chatWithID;
    }

    public List<MessageItem> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<MessageItem> messagesList) {
        this.messagesList = messagesList;
    }
}
