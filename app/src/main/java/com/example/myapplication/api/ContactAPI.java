package com.example.myapplication.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.State.LoggedUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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


    public void getAllContacts(String token){
        Call<JsonArray> call = webServiceAPI.getContactsList("bearer " + token);
        Log.d("ContactAPI", "In contactAPI get all contacts, token: "+token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("ContactAPI", "Response value: "+response.body());
                new Thread(() -> {
                    String jsonString = response.body().toString();
                    Log.i("onResponse", jsonString);
                    Type listType = new TypeToken<List<Contact>>() {}.getType();
                    List<Contact> yourList = new Gson().fromJson(jsonString, listType);
                    Log.i("onResponse", yourList.toString());
//                    contactList = yourList;

                    if (response.body() != null) {
                        contactDao.deleteAllContacts();
                        contactDao.insertContactList(yourList);
                        contactList.postValue(contactDao.getAllContacts());
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("ContactAPI", "Response error: "+t);
            }
        });
    }
    public void addContact(Contact contact) {
        Call<Contact> call = webServiceAPI.addContact(LoggedUser.getUserName());
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {

            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {

            }
        });
    }
    public  MutableLiveData<List<Contact>> getContactList() {
        return contactList;
    }
//    public void add(final Contact contact) {
//        // add contact to local db
//        contactDao.insert(contact);
//        // add contact to remote db
//        webServiceAPI.addContact(contact);
//    }
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

}