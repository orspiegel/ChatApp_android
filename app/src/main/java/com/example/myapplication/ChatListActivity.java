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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entites.Contact;
import com.example.myapplication.State.LoggedUser;
import com.example.myapplication.Utils.Utils;
import com.example.myapplication.ViewModels.ContactViewModel;
import com.example.myapplication.recyclerview.ContactRecyclerViewAdapter;

import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private List<Contact> contacts;
    private ContactRecyclerViewAdapter adapter;
    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        ImageView imgView = findViewById(R.id.SpeakerImg);
        TextView nameView = findViewById(R.id.speakerName);
        nameView.setText(LoggedUser.getDisplayName());
        imgView.setImageBitmap(Utils.StringToBitMap(LoggedUser.getProfilePic()));
        RecyclerView recyclerView = findViewById(R.id.rvContactRecyclerView);
        adapter = new ContactRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("ChatListActivity", "msg");
        ImageView logOutBtn = findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Add contact
        ImageView addContactBtn = findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatListActivity.this);
            alertDialogBuilder.setTitle("Add contact");
            final EditText input = new EditText(ChatListActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            alertDialogBuilder.setView(input);
            alertDialogBuilder.setPositiveButton("Add", (dialog, which) -> {
                String enteredContactName = input.getText().toString().trim();
                contactViewModel.add(enteredContactName);
                dialog.dismiss();
            });
            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        // OnClick action - enter user chat
        adapter.setOnContactClickListener(contact -> {
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("contactName", contact.getContactName());
            startActivity(intent);
        });

        // Get contact list
        contactViewModel.getAllContacts().observe(this, newContactsList -> {
            contacts = newContactsList;
            adapter.setContactList(contacts);
            recyclerView.setAdapter(adapter);
        });

    }
}
