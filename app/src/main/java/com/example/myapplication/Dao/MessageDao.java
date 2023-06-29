package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entites.Chat;
import com.example.myapplication.Entites.Message;

import java.util.List;

@Dao
public interface MessageDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("SELECT * FROM messages_table WHERE msgID = :id")
    Message getMessage(String id);
    @Query("SELECT * FROM messages_table")
    List<Message> getAllMessages();
}
