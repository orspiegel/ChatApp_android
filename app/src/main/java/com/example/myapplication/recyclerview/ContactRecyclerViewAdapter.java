package com.example.myapplication.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.R;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public ContactRecyclerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.tvContactName.setText(contact.getContactName());
        holder.tvContactLastMsg.setText(contact.getLastMsg());
        holder.tvContactLastDate.setText(contact.getLastMsgDate());

        holder.itemView.setOnClickListener(v -> {
            Log.d("ContactRecyclerView", "contact clicked name" + contact.getContactName());
            // Create an Intent to start the ChatActivity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("contactName", contact.getContactName());
            // Add any other necessary data
            // Start the ChatActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvContactName, tvContactLastMsg, tvContactLastDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.avatarView);
            tvContactName = itemView.findViewById(R.id.contactName);
            tvContactLastMsg = itemView.findViewById(R.id.lastMsg);
            tvContactLastDate = itemView.findViewById(R.id.lastMsgDate);
        }
    }
}
