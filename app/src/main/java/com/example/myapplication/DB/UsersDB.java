package com.example.myapplication.DB;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.Dao.UserDao;
import com.example.myapplication.Entites.User;

@Database(entities = {User.class}, version = 1)
public abstract class UsersDB extends RoomDatabase {
    private static UsersDB instance;
    public abstract UserDao userDao();
    public static synchronized UsersDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UsersDB.class, "users_database").fallbackToDestructiveMigration()
                    .addCallback(roomCallback).build();
        }
        return instance;
    };
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateDBAsyncTask(instance).execute();
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;

        private PopulateDBAsyncTask(UsersDB db) {
            userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("username", "userDisplayName", "password", "profilePic", "token"));
            return  null;
        }

    };
}

