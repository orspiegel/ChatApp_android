package com.example.myapplication.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entites.Message;
import com.example.myapplication.R;
import com.example.myapplication.State.LoggedUser;

import java.util.ArrayList;
import java.util.List;
//
//public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder> {
//    private Context context;
//    private List<Message> messageList;
//    private LayoutInflater inflater;
//
//    public MessageRecyclerViewAdapter(Context context, List<Message> messageList) {
//        inflater = LayoutInflater.from(context);
//        this.context = context;
//        this.messageList = new ArrayList<>();
//        this.messageList.addAll(messageList);
////        this.messageList = messageList;
////        notifyDataSetChanged();
//        Log.d("adapter", "Entered to adapter");
//    }
//
//
//    public void setMessageList(List<Message> messageList) {
//        this.messageList = messageList;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, boolean me) {
//        if (me == true) {
//            View view = inflater.inflate(R.layout.message_wrapper, parent, false);
//
//
//
//        }
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.message_wrapper, parent, false);
//        return new MessageRecyclerViewAdapter.MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageRecyclerViewAdapter.MyViewHolder holder, int position) {
//        Message message = messageList.get(position);
//
//        Log.d("userName", message.getSenderUserName());
//        Log.d("sender name&content", message.getSenderUserName()+" "+message.getContent());
//        //Log.d("userName", message.getMsgID());
//        //Log.d("userName", message.getCreated().getClass().toString());
//        //Log.d("userName", message.getContent());
//        holder.sentByMe = (message.getSenderUserName().equals(LoggedUser.getUserName()));
//        //holder.sentByMe = false;
//        holder.timeStamp.setText(message.getCreated());
//        holder.msgContent.setText(message.getContent());
//        //holder.senderName.setText(message.s);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        if (messageList != null) {
//            return messageList.size();
//        } else {
//            return 0;
//        }
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView timeStamp, msgContent, senderName;
//        public boolean sentByMe;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            if (sentByMe) {
//                timeStamp = itemView.findViewById(R.id.text_gchat_timestamp_me);
//                msgContent = itemView.findViewById(R.id.text_gchat_message_me);
//            } else {
//                timeStamp = itemView.findViewById(R.id.text_gchat_timestamp_other);
//                msgContent = itemView.findViewById(R.id.text_gchat_message_other);
//            }
//        }
//    }
//}
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflater;
    private ArrayList<Message> messages;

    class MessageFromOther extends RecyclerView.ViewHolder {
        private TextView content;
        private TextView created;

        MessageFromOther(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            created = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }
    }

    class MessageFromMe extends RecyclerView.ViewHolder {
        private TextView content;
        private TextView created;

        MessageFromMe(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            created = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }
    }


    public MessageAdapter(Context context, List<Message> messageList) {
        inflater = LayoutInflater.from(context);
        this.messages = new ArrayList<>();
        this.messages.addAll(messageList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, boolean me) {
        if (me == false) {
            View itemView = inflater.inflate(R.layout.other, parent, false);
            return new MessageFromOther(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.me, parent, false);
            return new MessageFromMe(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message current = messages.get(position);
        String YYYYMMDD = current.getCreated().substring(0,10);
        String hhmm = current.getCreated().substring(11,15);
        Log.d("messages content", current.getContent());
        if(current.getSenderUserName()!=LoggedUser.getUserName()) {
            MessageFromMe vh = (MessageFromMe)holder;
            vh.content.setText(current.getContent());
            vh.created.setText(hhmm);
        }
        else{
            MessageFromOther vh = (MessageFromOther)holder;
            vh.content.setText(current.getContent());
            vh.created.setText(hhmm);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}