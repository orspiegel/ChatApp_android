package com.example.myapplication.Entites;

public class ContactWrapper {
    int id;
    UserData user;

    public ContactWrapper(int id, UserData user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
