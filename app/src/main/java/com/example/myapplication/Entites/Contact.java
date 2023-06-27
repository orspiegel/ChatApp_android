package com.example.myapplication.Entites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
/*
    Usage: GET baseURL/api/Chats/chatWithID/Messages
*/
@Entity(tableName = "contacts_table")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName;
    private String contactName;
    private String lastMsg;
    private String lastMsgDate;
    private String contactPic;
    private String autoID;

    public Contact(int id, String contactName, String lastMsg, String lastMsgDate, String contactPic, String userName, String autoID) {
        this.id = id;
        this.contactName = contactName;
        this.userName = userName;
        this.lastMsg = lastMsg;
        this.lastMsgDate = lastMsgDate;
        this.contactPic = contactPic;
        this.autoID = autoID;
    }

    public Contact() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public int getid() {
        return id;
    }

    public String getAutoID() {
        return autoID;
    }

    public void setAutoID(String autoID) {
        this.autoID = autoID;
    }
}
