package com.example.myapplication.Entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import java.util.List;

/*
Usage: GET baseURL/api/Chats/chatWithID/Messages
* */
@Entity(tableName = "chats_table")
public class Chat {

    @PrimaryKey
    @NonNull
    private String id;

    public Chat(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    /*@Relation(parentColumn = "id", entityColumn = "chatWithID", entity = User.class)
    private List<User> users;*/

    /*@Relation(parentColumn = "id", entityColumn = "chat_id", entity = Message.class)
    private List<Message> messages;*/
}