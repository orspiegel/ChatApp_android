package com.example.myapplication.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.DB.ContactsDB;
import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.MyApplication;
import com.example.myapplication.api.ContactAPI;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private ContactListData allContacts;
    private ContactsDB contactsDB;
    private ContactAPI contactAPI;


    public ContactRepository(Application application) {
        contactsDB = ContactsDB.getInstance(application);
        contactDao = contactsDB.contactDao();
        allContacts = new ContactListData();
        contactAPI = new ContactAPI(contactDao, allContacts);
    }
    public void insert(Contact contact) {
        new InsertContactAsyncTask(contactDao, contactAPI).execute(contact);
    }
//    public void update(Contact contact) {
//        new UpdateContactAsyncTask(contactDao, contactAPI).execute(contact);
//    }
//    public void delete(Contact contact) {
//        new DeleteContactAsyncTask(contactDao, contactAPI).execute(contact);
//    }
//    public void deleteAllContacts() {
//        new DeleteAllContactAsyncTask(contactDao, contactAPI).execute();
//    }
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDao contactDao;
        private ContactAPI contactAPI;
        private InsertContactAsyncTask(ContactDao contactDao, ContactAPI contactAPI) {
            this.contactDao = contactDao;
            this.contactAPI = contactAPI;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.insert(contacts[0]);
            contactAPI.addContact(contacts[0]);
            return null;
        }
    }

//    private static class DeleteAllContactAsyncTask extends AsyncTask<Void, Void, Void> {
//        private ContactDao contactDao;
//        private ContactAPI contactAPI;
//        private DeleteAllContactAsyncTask(ContactDao contactDao, ContactAPI contactAPI) {
//            this.contactDao = contactDao;
//            this.contactAPI = contactAPI;
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            contactDao.deleteAllContacts();
//            contactAPI.delete();
//            return null;
//        }
//    }

    private static class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDao contactDao;
        private ContactAPI contactAPI;
        private UpdateContactAsyncTask(ContactDao contactDao, ContactAPI contactAPI) {
            this.contactDao = contactDao;
            this.contactAPI = contactAPI;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.update(contacts[0]);
            contactAPI.update(contacts[0]);
            return null;
        }
    }

    private static class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDao contactDao;
        private ContactAPI contactAPI;
        private DeleteContactAsyncTask(ContactDao contactDao, ContactAPI contactAPI) {
            this.contactDao = contactDao;
            this.contactAPI = contactAPI;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.delete(contacts[0]);
            contactAPI.delete(contacts[0]);
            return null;
        }
    }
    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            // local database
            List<Contact> contacts = contactDao.getAllContacts();
            setValue(contacts);
        }
        @Override
        protected void onActive() {
            super.onActive();
            //change the db to the one containing the info from the server
            contactsDB = ContactsDB.getInstance(MyApplication.context);
            contactDao = contactsDB.contactDao();
            // update the mutable live data
            allContacts.postValue(contactDao.getAllContacts());
            new Thread(() -> {
                // not local database
                contactAPI.getAllContacts(MyApplication.getToken());
            }).start();
        }
    }

    public LiveData<List<Contact>> getAll() {
        return allContacts;
    }
    public void add(final Contact contact) {
        // add contact to local db
        contactDao.insert(contact);
        // add contact to remote db
        contactAPI.addContact(contact);
    }
    public void update(final Contact contact) {
        // update contact on local db
        contactDao.update(contact);
        // update contact on remote db
        contactAPI.update(contact);
    }

    public void delete(final Contact contact) {
        // delete contact from local db
        contactDao.delete(contact);
        // delete contact from remote db
        contactAPI.delete(contact);
    }

    public void reload() {
        contactAPI.getAllContacts(MyApplication.getToken());
    }
}
