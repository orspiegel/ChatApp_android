package com.example.myapplication.DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.Dao.ChatsDao;
import com.example.myapplication.Entites.Chat;

@Database(entities = {Chat.class}, version = 30)
public abstract class ChatDB extends RoomDatabase {

    private static ChatDB instance;


    public abstract ChatsDao chatDao();

    public static synchronized ChatDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ChatDB.class, "chats_database").fallbackToDestructiveMigration()

                    .addCallback(roomCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new ChatDB.PopulateDBAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ChatsDao chatDao;

        private PopulateDBAsyncTask(ChatDB db) {
            chatDao = db.chatDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return  null;
        }

    }
}