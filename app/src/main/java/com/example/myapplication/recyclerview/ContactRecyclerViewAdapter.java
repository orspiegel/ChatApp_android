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
import com.example.myapplication.R;
import com.example.myapplication.ROOM_p.Contact;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<Contact> contactModels;
    private String loggedInId;

    public ContactRecyclerViewAdapter(Context context, List<Contact> contactModels, String loggedInId) {
        this.context = context;
        this.contactModels = contactModels;
        this.loggedInId = loggedInId;
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
        Contact contact = contactModels.get(position);

        holder.tvContactName.setText(contact.getDisplayName());
        holder.tvContactLastMsg.setText(contact.getLastMessageContent());
        holder.tvContactLastDate.setText(contact.getLastMessageCreated());


        holder.itemView.setOnClickListener(v -> {
            Log.d("ContactRecyclerView", "contact clicked name" +contact.getDisplayName());

            // Create an Intent to start the ChatActivity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("contactId", contact.getId());
            intent.putExtra("contactName", contact.getDisplayName());
            intent.putExtra("loggedInId", loggedInId);
            // Add any other necessary data

            // Start the ChatActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
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
