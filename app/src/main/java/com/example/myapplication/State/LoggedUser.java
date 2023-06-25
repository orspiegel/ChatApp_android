package com.example.myapplication.State;

import com.example.myapplication.Entites.Contact;

public class LoggedUser {
    static String userName;
    static String displayName;
    static String profilePic;
    static Contact currentContact;

    public static void setLoggedIn(String displayName, String profilePic) {
//        LoggedUser.userName = userName;
        LoggedUser.displayName = displayName;
        LoggedUser.profilePic = profilePic;
    }
    public static String getUserName() {
        return userName;
    }

    public static String getProfilePic() {
        return profilePic;
    }

    public static void setProfilePic(String profilePic) {
        LoggedUser.profilePic = profilePic;
    }

    public static void setUserName(String userName) {
        LoggedUser.userName = userName;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static void setDisplayName(String displayName) {
        LoggedUser.displayName = displayName;
    }

    public static Contact getCurrentContact() {
        return currentContact;
    }

    public static void setCurrentContact(Contact currentContact) {
        LoggedUser.currentContact = currentContact;
    }
}
