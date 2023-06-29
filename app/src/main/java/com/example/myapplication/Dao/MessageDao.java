package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entites.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Message message); // Changed return type to long

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("SELECT * FROM messages_table WHERE chat_id = :chatID ORDER BY timeStamp DESC LIMIT 1")
    Message getLatestMessageFromChat(String chatID);

    @Query("SELECT * FROM messages_table WHERE msgID = :id")
    Message getMessage(int id); // Changed parameter type to int as msgID is int

    @Query("SELECT * FROM messages_table")
    List<Message> getAllMessages();
}