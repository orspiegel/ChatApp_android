package com.example.myapplication.DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.Dao.MessageDao;
import com.example.myapplication.Entites.Chat;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.Dao.MessageDao;
@Database(entities = {Message.class}, version = 1)

public abstract class MessageDB extends RoomDatabase {

    private static MessageDB instance;


    public abstract MessageDao messageDao();

    public static synchronized MessageDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDB.class, "chats_database").fallbackToDestructiveMigration()

                    .addCallback(roomCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new MessageDB.PopulateDBAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private MessageDao messageDao;

        private PopulateDBAsyncTask(MessageDB db) {
            messageDao = db.messageDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return  null;
        }

    }

}
