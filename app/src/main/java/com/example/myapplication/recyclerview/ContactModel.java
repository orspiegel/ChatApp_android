package com.example.myapplication.recyclerview;

public class ContactModel {
    String contactName;
    String lastMsg;
    String lastMsgDate;
    int contactImg;


    public ContactModel(String contactName, String lastMsg, String lastMsgDate, int contactImg) {
        this.contactName = contactName;
        this.lastMsg = lastMsg;
        this.lastMsgDate = lastMsgDate;
        this.contactImg = contactImg;
    }

    public String getContactName() {
        return contactName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public String getLastMsgDate() {
        return lastMsgDate;
    }

    public int getContactImg() {
        return contactImg;
    }
}
