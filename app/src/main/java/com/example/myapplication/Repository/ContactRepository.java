package com.example.myapplication.Repository;

import android.app.Application;

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


    public ContactRepository(Application application)  {
        contactsDB = ContactsDB.getInstance(application);
        contactDao = contactsDB.contactDao();
        allContacts = new ContactListData();
        contactAPI = new ContactAPI(contactDao, allContacts);
    }
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
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
//            new Thread(() -> {
//                // not local database
//                contactAPI.getAllContacts(MyApplication.getToken());
//            }).start();
        }
    }

    public LiveData<List<Contact>> getAll() {
        return allContacts;
    }
//    public void add(final Contact contact) {
//        // add contact to local db
//        contactDao.insert(contact);
//        // add contact to remote db
//        contactAPI.addContact(contact);
//    }
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

//    public void reload() {
//        contactAPI.getAllContacts(MyApplication.getToken());
//    }
}
