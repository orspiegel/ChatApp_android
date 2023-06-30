package com.example.myapplication.Objects;

public class User {
    String userName;
    String displayName;
    String profilePic;

    public User(String username, String displayName, String profilePic) {
        this.userName = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public User(User user) {
        this.userName = user.userName;
        this.displayName = user.displayName;
        this.profilePic = user.profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}


