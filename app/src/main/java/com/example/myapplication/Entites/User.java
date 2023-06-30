package com.example.myapplication.Entites;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "users_table")
public class User {
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private  String password;

    public String getUserName() {
        return userName;
    }
    @PrimaryKey
    @NonNull
    private  String userName;

//    private String userName;
    private String displayName;
//    private String password;
    private String profilePic;
//    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;


    public String getPassword() {
        return password;
    }

    public User() {
    }


    public User(@NonNull String userName, String displayName, String password, String profilePic) {
        this.userName = userName;
        this.displayName = displayName;
        this.password = password;
        this.profilePic = profilePic;
    }

//    public User(@NonNull String userName, String profilePic, String displayName, String id) {
////        this.userName = userName;
//        this.displayName = displayName;
//        this.profilePic = profilePic;
//        this.id = id;
//    }

    @NonNull
//    public String getUserName() {
//        return userName;
//    }

//    public void setUserName(@NonNull String userName) {
//        this.userName = userName;
//    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

//    public String getPassword() {
//        return password;
//    }

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
}
