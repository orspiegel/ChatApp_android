package com.example.myapplication.Entites;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.myapplication.Objects.MessageItem;

import java.util.Calendar;
import java.util.Date;


@Entity(tableName = "messages_table")
public class Message {

    @PrimaryKey
    @NonNull
    private String msgID;

    private String content;
    private String created;

    private String chat_id;

    @ColumnInfo(name = "senderUserName")
    private String senderUserName;

    public Message(@NonNull String msgID, String content, String created, String senderUserName, String chat_id) {
        this.msgID = msgID;
        this.content = content;
        this.created = created;
        this.senderUserName = senderUserName;
        this.chat_id = chat_id;
    }



    public Message(@NonNull MessageItem messageItem, String chat_id) {
        this.msgID = messageItem.getMsgID();
        this.created = messageItem.getCreated();
        this.content = messageItem.getContent();
        this.senderUserName = messageItem.getSender();
        this.chat_id = chat_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    @NonNull
    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(@NonNull String msgID) {
        this.msgID = msgID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }
}

/*@Entity(tableName = "messages_table")
public class Message {

    @NonNull@PrimaryKey(autoGenerate = true)
    private int msgID;

    @ColumnInfo(name = "chat_id")
    private String chat_id;
    private String timeStamp;
    private String content;
    private String senderUserName;

    public Message(String chat_id, String content, String senderUserName) {
        this.chat_id = chat_id;
        this.timeStamp  = Calendar.getInstance().getTime().toString();;
        this.content = content;
        this.senderUserName = senderUserName;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgID() {
        return msgID;
    }

    public void setMsgID(int msgID) {
        this.msgID = msgID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }
}*/