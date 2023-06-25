package com.example.myapplication.Entites;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages_table")
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String created;
    private String senderUsername;
    private String msg;
    private String contactId;

    public Message(int id, String msg, String senderUsername, String contactId, String created) {
        this.id = id;
        this.created = created;
        this.senderUsername = senderUsername;
        this.msg = msg;
        this.contactId = contactId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
