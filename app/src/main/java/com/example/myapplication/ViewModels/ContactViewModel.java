package com.example.myapplication.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entites.Contact;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Repository.ContactRepository;

import java.io.Serializable;
import java.util.List;

public class ContactViewModel extends ViewModel implements Serializable {
    private ContactRepository repository;
    private LiveData<List<Contact>> allContacts;

    public ContactViewModel() {
        repository = new ContactRepository(MyApplication.context);
        allContacts = repository.getAllContacts();
    }
    public void update(Contact contact) {
        repository.update(contact);
    }
    public void delete(Contact contact) {
        repository.delete(contact);
    }
    public LiveData<List<Contact>> getAllContacts () {
        return allContacts;
    }

    public void add(String name) {
        repository.add(name);
    }
}
