package com.example.myapplication.ROOM_p;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contact WHERE contactId = :contactId")
    Contact get(String contactId);

    @Query("SELECT * FROM contact WHERE loggedInID = :userId")
    List<Contact> getContactsByUserId(String userId);

    @Insert
    void insert(Contact...contacts);
    @Update
    void update(Contact...contacts);
    @Delete
    void delete(Contact...contacts);
}
