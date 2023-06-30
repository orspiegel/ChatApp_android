package com.example.myapplication.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.ChatListActivity;
import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Objects.AddContactRequest;
import com.example.myapplication.Objects.AddContactResponse;
import com.example.myapplication.Objects.ChatResponse;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.Objects.MessageResponse;
import com.example.myapplication.Objects.getAllMessagesResponse;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.example.myapplication.ViewModels.BaseUrlInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private ContactDao contactDao;
    private MutableLiveData<List<Contact>> contactList;

    public ContactAPI(ContactDao contactDao,MutableLiveData<List<Contact>> contactList){
        SharedPreferences prefs = MyApplication.context.getSharedPreferences("AppSettings", MODE_PRIVATE);
        String savedBaseUrl = prefs.getString("baseUrl", "http://10.0.2.2:5000/api/");
        BaseUrlInterceptor.getInstance().setBaseUrl(savedBaseUrl);
        String url = MyApplication.context.getString(R.string.baseUrl);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(BaseUrlInterceptor.getInstance())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.contactDao = contactDao;
        this.contactList = contactList;
    }
    public void getAllContacts(){
        String token = MyApplication.getToken();
        Call<List<ChatResponse>> call = webServiceAPI.getContactsList("bearer " + token);
        Log.d("ContactAPI", "In contactAPI get all contacts, token: "+token);
        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                Log.d("ContactAPI", "Response value: " + response.body());
                new Thread(() -> {
                    if (response.body() != null) {
                        List<Contact> contParsedList = new ArrayList<>();
                        for (ChatResponse c : response.body()) {

                            Log.i("Chat response", "Res: "+c.getUser().getDisplayName());
                            Log.i("Chat response", "Res: "+c.getId());
                            Contact contact = new Contact();
                            contact.setContactName(c.getUser().getDisplayName());
                            contact.setUserName(c.getUser().getUsername());
                            contact.setContactPic(c.getUser().getProfilePic());
                            contact.setAutoID(c.getId());
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
                        contactList.postValue(contactDao.getAllContacts());

                    }
                }).start();
            }
            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {
                Log.d("ContactAPI", "Response error: "+t);
                Toast.makeText(MyApplication.context, "Error in displaying Contact list", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addContact(String username) {
        String token = MyApplication.getToken();
        Log.d("Add contact", "input token "+token);
        AddContactRequest request = new AddContactRequest(username);
        Log.d("Contact api add","body " +request );
        Call<AddContactResponse> call = webServiceAPI.addContact("bearer " + token, request);
        call.enqueue(new Callback<AddContactResponse>() {
            @Override
            public void onResponse(Call<AddContactResponse> call, Response<AddContactResponse> response) {
                Log.d("onResponse add contact","response"+response);

                if (response.code() == 200) {
                    if (response.body() != null) {
                        Log.d("Add contact", "Added contact: " + response.body());
                        Log.d("Add contact", "Added contact: " + response.body().getId());
                        Log.d("Add contact", "Added contact: " + response.body().getContactResponse().getDisplayName());
                        Log.d("Add contact", "Added contact: " + response.body().getContactResponse().getUsername());
                        Contact contact = new Contact(response.body());
                        new Thread(() -> {
                            contactDao.insert(contact);
                        }).start();
                        List<Contact> newContactList = contactList.getValue();
                        newContactList.add(contact);
                        contactList.postValue(newContactList);
                    }
                } else {
                    Toast.makeText(MyApplication.context, "Select a valid contact - " +
                            "another signed up user not already in your contacts list", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddContactResponse> call, Throwable t) {
                Log.d("AddContact", "Error! " + t);
                Toast.makeText(MyApplication.context, "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void update(Contact contact) {
    }

    public void delete(Contact contact) {
    }

    public interface ContactsCallback {
        void onContactsRecieved();
    }


}