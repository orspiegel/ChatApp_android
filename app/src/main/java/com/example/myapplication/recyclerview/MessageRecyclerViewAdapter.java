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
import com.example.myapplication.State.LoggedUser;
import com.example.myapplication.Utils.Utils;

import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder> {

    private Context context;

    private List<Message> messageList;

    public MessageRecyclerViewAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        notifyDataSetChanged();
        Log.d("adapter", "Entered to adapter");
    }


    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message_wrapper, parent, false);
        return new MessageRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerViewAdapter.MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        //Log.d("userName", message.getSenderUserName());
        //Log.d("userName", LoggedUser.getUserName());
        //Log.d("userName", message.getMsgID());
        //Log.d("userName", message.getCreated().getClass().toString());
        //Log.d("userName", message.getContent());
        holder.sentByMe = (message.getSenderUserName().equals(LoggedUser.getUserName()));
        //holder.sentByMe = false;
        holder.timeStamp.setText(message.getCreated());
        holder.msgContent.setText(message.getContent());
        //holder.senderName.setText(message.s);


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
        public boolean sentByMe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            if (sentByMe) {
                timeStamp = itemView.findViewById(R.id.text_gchat_timestamp_me);
                msgContent = itemView.findViewById(R.id.text_gchat_message_me);
            } else {
                timeStamp = itemView.findViewById(R.id.text_gchat_timestamp_other);
                msgContent = itemView.findViewById(R.id.text_gchat_message_other);
            }
        }
    }
}