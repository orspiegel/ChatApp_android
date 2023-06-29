package com.example.myapplication.Entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/*
Usage: GET baseURL/api/Chats/chatWithID/Messages
* */

@Entity(tableName = "chats_table")
public class Chat {

    @PrimaryKey @NonNull
    private String chatWithID;

    public Chat() {}

    public Chat(String chatWithID) {
        this.chatWithID = chatWithID;
    }

    public String getChatWithID() {
        return chatWithID;
    }

    public void setChatWithID(String chatWithID) {
        this.chatWithID = chatWithID;
    }
}