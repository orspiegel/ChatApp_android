package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entites.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact... contacts);

    @Insert
    void insertContactList(List<Contact> contactList);
    @Update
    void update(Contact... contacts);
    @Delete
    void delete(Contact... contacts);
    @Query("DELETE FROM contacts_table")
    void deleteAllContacts();
    @Query("SELECT * FROM contacts_table")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contacts_table WHERE id = :id")
    Contact getContact(String id);
}
