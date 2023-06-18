package com.example.myapplication.ROOM_p;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE id = :id")
    User get(String id);

    @Insert
    void insert(User...user);
    @Update
    void update(User...user);
    @Delete
    void delete(User...user);
}
