package com.example.myapplication.Objects;

import com.example.myapplication.Entites.LastMessage;

public class ChatResponse {
    int id;
    ContactResponse user;
    LastMessage lastMessage;

    public ChatResponse(int id, ContactResponse user, LastMessage lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }

    public ChatResponse(int id, ContactResponse user) {
        this.id = id;
        this.user = user;
        this.lastMessage = null;
    }

    public ChatResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContactResponse getUser() {
        return user;
    }

    public void setUser(ContactResponse user) {
        this.user = user;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }
}
