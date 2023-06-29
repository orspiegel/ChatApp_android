package com.example.myapplication.Entites;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages_table")
public class Message {

    @NonNull@PrimaryKey(autoGenerate = false)
    private String msgID;

    @ColumnInfo(name = "chat_id")
    private String chat_id;
    private String timeStamp;
    private String content;
    private String senderID;

    public Message(String msgID, String chat_id, String timeStamp, String content, String senderID) {
        this.msgID = msgID;
        this.chat_id = chat_id;
        this.timeStamp = timeStamp;
        this.content = content;
        this.senderID = senderID;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
