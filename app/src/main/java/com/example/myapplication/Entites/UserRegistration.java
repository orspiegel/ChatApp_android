package com.example.myapplication.Entites;

public class UserRegistration {
    private final String username;
    private final String password;
    private final String displayName;
    private final String profilePic;

    public UserRegistration(String username, String password, String displayName, String profilePic) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }
}