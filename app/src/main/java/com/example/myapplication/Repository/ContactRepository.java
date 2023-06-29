package com.example.myapplication.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.DB.ContactsDB;
import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.api.ContactAPI;

import java.util.LinkedList;
import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private ContactListData allContacts;
    private ContactsDB contactsDB;
    private ContactAPI contactAPI;


    public ContactRepository(/*Application application*/Context context)  {
        /*
        * contactList = new MutableLiveData<>();

        contactDB = ContactsDB.getInstance(this);
        contactDao = contactDB.contactDao();
        contactAPI = new ContactAPI(contactDao, contactList);*/
        contactsDB = ContactsDB.getInstance(context);
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
            setValue(new LinkedList<>());
        }
        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                allContacts.postValue(contactDao.getAllContacts());
                contactAPI.getAllContacts();
            }).start();
        }
    }

    public LiveData<List<Contact>> getAll() {
        return allContacts;
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

    public void add(String name) {
        contactAPI.addContact(name);
    }

}
