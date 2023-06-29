package com.example.myapplication.Objects;

import com.example.myapplication.Entites.LastMessage;
public class MessageResponse {

    private String id;
    private String content;
    private String created;

    public MessageResponse(String id, String content, String created, String sender) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    private String sender;

}
