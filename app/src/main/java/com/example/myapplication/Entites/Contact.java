package com.example.myapplication.Entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/*
    Usage: GET baseURL/api/Chats/chatWithID/Messages
*/
@Entity(tableName = "contacts_table")
public class Contact {
    @PrimaryKey
    @NonNull
    private String id;
    private String contactName;
    private String lastMsg;
    private String lastMsgDate;
    private String contactPic;

    public Contact(String id, String contactName, String lastMsg, String lastMsgDate, String contactPic) {
        this.id = id;
        this.contactName = contactName;
        this.lastMsg = lastMsg;
        this.lastMsgDate = lastMsgDate;
        this.contactPic = contactPic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgDate() {
        return lastMsgDate;
    }

    public void setLastMsgDate(String lastMsgDate) {
        this.lastMsgDate = lastMsgDate;
    }

    public String getContactPic() {
        return contactPic;
    }

    public void setContactPic(String contactPic) {
        this.contactPic = contactPic;
    }
    public String getid() {
        return id;
    }

}
