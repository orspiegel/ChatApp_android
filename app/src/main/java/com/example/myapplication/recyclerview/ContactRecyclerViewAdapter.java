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
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.Entites.Contact;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Contact> contactList;
    private OnContactClickListener onContactClickListener; // New listener

    private String currentUser;

    public ContactRecyclerViewAdapter(Context context) {
        this.context = context;
        Log.d("adapter", "Entered to adapter");
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    public void setOnContactClickListener(OnContactClickListener listener) {
        this.onContactClickListener = listener;
    }

    public void setCurrentUser(Object loggedInUserName) {
        currentUser = (String) loggedInUserName;
    }

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        holder.imageView.setImageBitmap(Utils.StringToBitMap(contact.getContactPic()));

        holder.itemView.setOnClickListener(v -> {
            Intent clickedContact = new Intent(v.getContext(), ChatActivity.class);
            clickedContact.putExtra("currentUserName", this.currentUser);
            clickedContact.putExtra("serverChatID", contact.getAutoID());
            clickedContact.putExtra("displayName", contact.getContactName());
            clickedContact.putExtra("profilePic", contact.getContactPic());

            v.getContext().startActivity(clickedContact);

        });
    }

    @Override
    public int getItemCount() {
        if (contactList != null) {
            return contactList.size();
        } else {
            return 0;
        }
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
