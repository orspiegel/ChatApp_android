package com.example.myapplication.recyclerview;
public class Message {
    private String id;
    private String content;
    private String created;
    private boolean sentByMe;

    public Message(String id, String content, String created, boolean sentByMe) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.sentByMe = sentByMe;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }
}
