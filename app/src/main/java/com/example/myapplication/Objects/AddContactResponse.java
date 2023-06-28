package com.example.myapplication.Objects;

public class AddContactResponse {
    String id;
    ContactResponse user;

    public AddContactResponse(String id, ContactResponse contactResponse) {
        this.id = id;
        this.user = contactResponse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContactResponse getContactResponse() {
        return user;
    }

    public void setContactResponse(ContactResponse contactResponse) {
        this.user = contactResponse;
    }
}
