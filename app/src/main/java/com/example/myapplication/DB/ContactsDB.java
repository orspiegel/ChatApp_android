package com.example.myapplication.DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;

@Database(entities = {Contact.class}, version = 60)
public abstract class ContactsDB extends RoomDatabase {
    private static ContactsDB instance;
    public abstract ContactDao contactDao();
    public static synchronized ContactsDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                            ContactsDB.class, "contacts_database").fallbackToDestructiveMigration()
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
        private ContactDao contactDao;

        private PopulateDBAsyncTask(ContactsDB db) {
            contactDao = db.contactDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            contactDao.insert(new Contact(1, "contactDisplayName", "bye", "14/10", "contactPic"));
            return  null;
        }

    };
}

