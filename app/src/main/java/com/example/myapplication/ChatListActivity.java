package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.ContactsDB;
import com.example.myapplication.Dao.ContactDao;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.State.LoggedUser;
import com.example.myapplication.Utils.Utils;
import com.example.myapplication.api.ContactAPI;
import com.example.myapplication.recyclerview.ContactRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity implements ContactAPI.ContactsCallback {
    private MutableLiveData<List<Contact>> contactList;
    private List<Contact> contacts;
    private ContactRecyclerViewAdapter adapter;

    private ContactDao contactDao;
    private ContactsDB contactDB;
    private ContactAPI contactAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        contactList = new MutableLiveData<>();

        contactDB = ContactsDB.getInstance(this);
        contactDao = contactDB.contactDao();
        contactAPI = new ContactAPI(contactDao, contactList);

        ImageView imgView = findViewById(R.id.SpeakerImg);
        TextView nameView = findViewById(R.id.speakerName);
        nameView.setText(LoggedUser.getDisplayName());
        imgView.setImageBitmap(Utils.StringToBitMap(LoggedUser.getProfilePic()));

        RecyclerView recyclerView = findViewById(R.id.rvContactRecyclerView);
        adapter = new ContactRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d("ChatListActivity", "msg");

        new Thread(() -> {
            contactAPI.getAllContacts(MyApplication.getToken(), this);
        }).start();

        ImageView logOutBtn = findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(v -> {
            // navigate to Login Activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
//
        ImageView addContactBtn = findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatListActivity.this);
            alertDialogBuilder.setTitle("Add contact");

            // Create the input field
            final EditText input = new EditText(ChatListActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            alertDialogBuilder.setView(input);
            alertDialogBuilder.setPositiveButton("Add", (dialog, which) -> {
                // Retrieve the entered contact name
                String enteredContactName = input.getText().toString().trim();
                // Execute the AddContactAsyncTask to perform the network operation in the background
                contactAPI.addContact(enteredContactName);
                // Dismiss the dialog
                dialog.dismiss();
            });

            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        adapter.setOnContactClickListener(contact -> {
            // Handle the contact click event here
            // For example, start the ChatActivity with the clicked contact
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("contactName", contact.getContactName());
            startActivity(intent);
        });

    }
    private void replaceOldListWithNewList() {
        // clear old list
        data.clear();

        // add new list
        ArrayList<String> newList = new ArrayList<>();
        newList.add("Lion");
        newList.add("Wolf");
        newList.add("Bear");
        data.addAll(newList);

        // notify adapter
        adapter.notifyDataSetChanged();
    }

    public void onContactsRecieved() {
        contacts = contactDao.getAllContacts();
        Log.d("ChatListActivity", "contacts on DB:"+contacts);
        Log.d("ChatListActivity", "contacts size on DB:"+contacts.size());

        runOnUiThread(()-> {
            adapter.setContactList(contacts);
            adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
        });
    }

}
