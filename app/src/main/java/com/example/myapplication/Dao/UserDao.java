package com.example.myapplication.Dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entites.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);
    @Update
    void update(User user);
    @Delete
    void delete(User user);
    @Query("DELETE FROM users_table")
    void deleteAllUsers();
    @Query("SELECT * FROM users_table")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users_table WHERE displayName = :displayname AND profilePic = :profilepic")
    User getUserByUsernameAndPassword(String displayname, String profilepic);

}
