package com.example.myapplication.Entites;

public class LastMessage {
    int id;
    String created;
    String content;

    public LastMessage(int id, String created, String content) {
        this.id = id;
        this.created = created;
        this.content = content;
    }

    public LastMessage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
