package com.example.myapplication.State;

public class CurrentContact {
    public static String id;
    public static String displayName;
    public static String profilePic;
    public static boolean isClicked;

    public static void setCurrentContact(String id, String displayName, String profilePic) {
        CurrentContact.id = id;
        CurrentContact.displayName = displayName;
        CurrentContact.profilePic = profilePic;
        CurrentContact.isClicked = true;
    }


    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        CurrentContact.id = id;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static void setDisplayName(String displayName) {
        CurrentContact.displayName = displayName;
    }

    public static String getProfilePic() {
        return profilePic;
    }

    public static void setProfilePic(String profilePic) {
        CurrentContact.profilePic = profilePic;
    }
}
