package com.example.myapplication.Objects;

import java.util.List;

public class ConversationPageResponse {

    String _id;
    List<String> users;
    List<MessageResponse> messages;

    public ConversationPageResponse(String id, List<String> users, List<MessageResponse> messages) {
        this._id = id;
        this.users = users;
        this.messages = messages;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<MessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageResponse> messages) {
        this.messages = messages;
    }
}

