package com.example.myapplication.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Objects.ChatResponse;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    private Retrofit retrofit;
    // remote
    private WebServiceAPI webServiceAPI;
    // local
    private ContactDao contactDao;
    // local
    private MutableLiveData<List<Contact>> contactList;

    public ContactAPI(ContactDao contactDao,MutableLiveData<List<Contact>> contactList){
        String url = MyApplication.context.getString(R.string.baseUrl);
        retrofit = (new Retrofit.Builder().baseUrl(url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.contactDao = contactDao;
        this.contactList = contactList;
    }
    public void getAllContacts(String token, ContactsCallback callback){
        Call<List<ChatResponse>> call = webServiceAPI.getContactsList("bearer " + token);
        Log.d("ContactAPI", "In contactAPI get all contacts, token: "+token);
        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                Log.d("ContactAPI", "Response value: " + response.body());
                new Thread(() -> {
                    if (response.body() != null) {
                        List<Contact> contParsedList = new ArrayList<>();
                        Log.d("Contacts", "contact list response type: "+response.body());
                        for (ChatResponse c : response.body()) {
                            Log.i("Chat response", "Res: "+c);
                            Log.i("Chat response", "Res: "+c.getUser().getDisplayName());
                            Contact contact = new Contact();
                            contact.setContactName(c.getUser().getDisplayName());
                            contact.setUserName(c.getUser().getUsername());
                            contact.setContactPic(c.getUser().getProfilePic());
                            contact.setId(c.getId());
                            if (c.getLastMessage() != null) {
                                contact.setLastMsg(c.getLastMessage().getContent());
                                contact.setLastMsgDate(c.getLastMessage().getCreated());
                            }
                            contParsedList.add(contact);
                        }
                        contactDao.deleteAllContacts();
                        for (Contact c : contParsedList) {
                            Log.i("onResponse contact", "Contact " + c.getUserName());
                            contactDao.insert(c);
                            Log.d("ContactAPI", "Contact " + c.getContactName() + " inserted to contacts list");
                        }
                        callback.onContactsRecieved();
//
                        contactList.postValue(contactDao.getAllContacts());
                    }
                }).start();
            }
            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {
                Log.d("ContactAPI", "Response error: "+t);
            }
        });
    }
    public void addContact(String username) {
        String token = MyApplication.getToken();
        Log.d("Add contact", "input token "+token);
        Call<ChatResponse> call = webServiceAPI.addContact("bearer " + token, username);

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                Log.d("AddContactAPI", "Response value: " + response.body());
                new Thread(() -> {
                    if (response.body() != null) {
                        Log.d("Add contact", "Added contact: " + response.body());
                        Contact contact = new Contact();
                        contact.setContactName(response.body().getUser().getDisplayName());
                        contact.setUserName(response.body().getUser().getUsername());
                        contact.setContactPic(response.body().getUser().getProfilePic());
                        contact.setId(response.body().getId());
                        Log.i("Add contact", "Contact " + contact.getUserName());
                        contactDao.insert(contact);
                        Log.d("Add contact", "Contact " + contact.getContactName() + " inserted to contacts list");
                        contactList.postValue(contactDao.getAllContacts());
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Log.d("AddContact", "Error! "+t);
            }
        });
    }

    public void getContactChatContent(String id) {
        Call<List<MessageItem>> call = webServiceAPI.getContactMessages("bearer " + MyApplication.getToken(), id);
        call.enqueue(new Callback<List<MessageItem>>() {
            @Override
            public void onResponse(Call<List<MessageItem>> call, Response<List<MessageItem>> response) {
                new Thread(() -> {
                    if (response.body() != null) {
                        Log.d("Chat", "Chat response: " + response.body());

//                        Contact contact = new Contact();
//                        contact.setContactName(response.body().getUser().getDisplayName());
//                        contact.setUserName(response.body().getUser().getUsername());
//                        contact.setContactPic(response.body().getUser().getProfilePic());
//                        contact.setId(response.body().getId());
//                        Log.i("Add contact", "Contact " + contact.getUserName());
//                        contactDao.insert(contact);
//                        Log.d("Add contact", "Contact " + contact.getContactName() + " inserted to contacts list");
//                        contactList.postValue(contactDao.getAllContacts());
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<List<MessageItem>> call, Throwable t) {
                Log.d("Chat", "Error! "+t);
            }
        });
    }


//    public void update(final Contact contact) {
//        // update contact on local db
//        contactDao.update(contact);
//        // update contact on remote db
//        api.update(contact);
//    }
//
//    public void delete(final Contact contact) {
//        // delete contact from local db
//        contactDao.delete(contact);
//        // delete contact from remote db
//        webServiceAPI.deleteContact(contact.getId());
//    }
//
//    public void reload() {
//        api.get();
//    }
//
    public void update(Contact contact) {
    }

    public void delete(Contact contact) {
    }

    public interface ContactsCallback {
        void onContactsRecieved();
    }

}