package com.example.myapplication.recyclerview;

import static android.view.View.GONE;

import android.app.Application;
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

    private String currentUserName;

    private boolean sentByMe;

    public MessageRecyclerViewAdapter(Context context, List<Message> messageList, String currentUserName) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserName = currentUserName;
        notifyDataSetChanged();
        Log.d("adapter", "Entered to adapter");
    }


    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        this.sentByMe = sentByMe;
    }

    @NonNull
    @Override
    public MessageRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message_wrapper, parent, false);
        return new MessageRecyclerViewAdapter.MyViewHolder(view, isSentByMe());
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerViewAdapter.MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        //Log.d("1", message.getCurrentUserName());
        Log.d("2", currentUserName);
        Log.d("Checkcheck", String.valueOf(currentUserName.equals(message.getCurrentUserName())));
                setSentByMe(currentUserName.equals(message.getCurrentUserName()));
        holder.timeStamp.setText(message.getCreated());
        holder.msgContent.setText(message.getContent());
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

        public MyViewHolder(@NonNull View itemView, boolean sentByMe) {
            super(itemView);
            if (sentByMe) {
                timeStamp = itemView.findViewById(R.id.text_gchat_timestamp_me);
                msgContent = itemView.findViewById(R.id.text_gchat_message_me);
                timeStamp.setVisibility(View.VISIBLE);
                msgContent.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.text_gchat_message_other).setVisibility(View.GONE);
                itemView.findViewById(R.id.text_gchat_timestamp_other).setVisibility(GONE);
            } else {
                timeStamp = itemView.findViewById(R.id.text_gchat_timestamp_other);
                msgContent = itemView.findViewById(R.id.text_gchat_message_other);
                timeStamp.setVisibility(View.VISIBLE);
                msgContent.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.text_gchat_timestamp_me).setVisibility(View.GONE);
                itemView.findViewById(R.id.text_gchat_message_me).setVisibility(GONE);
            }
        }
    }
}