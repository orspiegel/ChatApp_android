package com.example.myapplication.ROOM_p;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
   public abstract UserDao userDao();
}
