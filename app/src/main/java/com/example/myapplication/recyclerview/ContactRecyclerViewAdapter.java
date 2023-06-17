package com.example.myapplication.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ROOM_p.Contact;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.MyViewHolder> {
    Context context;
   List<Contact> contactModels;

    public ContactRecyclerViewAdapter(Context context, List<Contact> contactModels) {
        this.context = context;
        this.contactModels = contactModels;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new ContactRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvContactName.setText(contactModels.get(position).getDisplayName());
        holder.tvContactLastMsg.setText(contactModels.get(position).getLastMessageContent());
        holder.tvContactLastDate.setText(contactModels.get(position).getLastMessageCreated());
//        holder.imageView.setImageResource(contactModels.get(position).getProfilePic());
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
//            imageView = itemView.findViewById(R.id.avatarView);
            tvContactName = itemView.findViewById(R.id.contactName);
            tvContactLastMsg = itemView.findViewById(R.id.lastMsg);
            tvContactLastDate = itemView.findViewById(R.id.lastMsgDate);
        }
    }
}
