package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myapplication.Entites.ChatAndMessages;

import java.util.List;

@Dao
public interface ChatAndMessagesDao {

    @Transaction
    @Query("SELECT * FROM chats_table")
    List<ChatAndMessages> getAllChatsWithMessages();

    @Transaction
    @Query("SELECT * FROM chats_table WHERE chatWithID=:chatID")
    ChatAndMessages getChatWithMessages(String chatID);
}

