package com.example.myapplication.recyclerview;

public class UserDataModel {
    String userName;
    String image;
    String displayName;
    String id;
    int img;


    public UserDataModel(String userName, String image, String displayName, String id) {
        this.userName = userName;
        this.image = image;
        this.displayName = displayName;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImage() {
        return image;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
