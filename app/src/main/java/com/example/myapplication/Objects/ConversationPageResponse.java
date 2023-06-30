package com.example.myapplication.Objects;

import java.util.List;

public class ConversationPageResponse {

    String id;
    List<User> users;
    List<MessageResponse> messages;

    public ConversationPageResponse(String id, List<User> users, List<MessageResponse> messages) {
        this.id = id;
        this.users = users;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<MessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageResponse> messages) {
        this.messages = messages;
    }
}

