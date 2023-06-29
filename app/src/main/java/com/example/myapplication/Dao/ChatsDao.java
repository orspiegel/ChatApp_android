package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entites.Chat;

import java.util.List;

@Dao
public interface ChatsDao
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Chat chat);

    @Update
    void update(Chat chat);

    @Delete
    void delete(Chat chat);

    @Query("SELECT * FROM chats_table WHERE chatWithID = :id")
    Chat getChat(String id);
    @Query("SELECT * FROM chats_table")
    List<Chat> getAllChats();



}
