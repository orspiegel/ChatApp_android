package com.example.myapplication.Objects;

import java.util.List;

public class MessageItem {
    int msgID;
    String created;
    String sender;
    String content;

    public MessageItem(int msgID, String created, String sender, String content) {
        this.msgID = msgID;
        this.created = created;
        this.sender = sender;
        this.content = content;
    }
    public MessageItem(int msgID, String created, List<String> sender, String content) {
        this.msgID = msgID;
        this.created = created;
        this.sender = sender.get(0);
        this.content = content;
    }
    public int getMsgID() {
        return msgID;
    }

    public void setMsgID(int msgID) {
        this.msgID = msgID;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
