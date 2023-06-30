package com.example.myapplication.Objects;

public class getAllMessagesResponse {
    String id;
    String created;

    User sender;

    String content;

    public getAllMessagesResponse(String id, String content, String created, User sender) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.sender = new User(sender);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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
}
