package com.example.myapplication.Objects;

public class getAllMessagesResponse {
    User sender;

    String content;
    String _id;
    String created;

    int __v;

    public getAllMessagesResponse(String id, String content, String created, User sender) {
        this._id = id;
        this.content = content;
        this.created = created;
        this.sender = sender;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int get_v() {
        return __v;
    }

    public void set_v(int _v) {
        this.__v = __v;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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
