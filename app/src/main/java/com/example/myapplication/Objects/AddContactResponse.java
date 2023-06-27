package com.example.myapplication.Objects;

public class AddContactResponse {
    String id;
    ContactResponse contactResponse;

    public AddContactResponse(String id, ContactResponse contactResponse) {
        this.id = id;
        this.contactResponse = contactResponse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContactResponse getContactResponse() {
        return contactResponse;
    }

    public void setContactResponse(ContactResponse contactResponse) {
        this.contactResponse = contactResponse;
    }
}
