package com.example.myapplication.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entites.Contact;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Utils;

import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder> {

    private Context context;

    private List<Message> messageList;
    private ContactRecyclerViewAdapter.OnContactClickListener onContactClickListener; // New listener

    public MessageRecyclerViewAdapter(Context context) {
        this.context = context;
        Log.d("adapter", "Entered to adapter");
    }

    public void setContactList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    public void setOnContactClickListener(ContactRecyclerViewAdapter.OnContactClickListener listener) {
        this.onContactClickListener = listener;
    }

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new MessageRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerViewAdapter.MyViewHolder holder, int position) {
        Message message = messageList.get(position);


    }

    @Override
    public int getItemCount() {
        if (messageList != null) {
            return messageList.size();
        } else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView timeStamp, msgContent, senderName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.senderTextView);
            timeStamp = itemView.findViewById(R.id.timestampTextView);
            msgContent = itemView.findViewById(R.id.contentTextView);
        }
    }
}
