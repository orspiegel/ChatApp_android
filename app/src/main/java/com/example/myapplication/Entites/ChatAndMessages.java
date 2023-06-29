package com.example.myapplication.Entites;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ChatAndMessages {
    @Embedded
    public Chat chat;

    @Relation(parentColumn = "id", entityColumn = "chat_id")
    public List<Message> messages;
}