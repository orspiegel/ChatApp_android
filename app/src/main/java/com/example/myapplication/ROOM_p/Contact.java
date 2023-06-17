package com.example.myapplication.ROOM_p;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate=true)
    private int contactId;
    private String displayName;
    private String id;
    private String profilePic;
    private String lastMessageId;
    private String lastMessageCreated;
    private String lastMessageContent;
    private String loggedInID;

    public Contact(String loggedInID, String displayName, String id, String profilePic, String lastMessageId, String lastMessageCreated, String lastMessageContent) {
        this.loggedInID = loggedInID;
        this.displayName = displayName;
        this.id = id;
        this.profilePic = profilePic;
        this.lastMessageId = lastMessageId;
        this.lastMessageCreated = lastMessageCreated;
        this.lastMessageContent = lastMessageContent;
    }
    public Contact(){}

    public String getLoggedInID() {
        return loggedInID;
    }

    public void setLoggedInID(String loggedInID) {
        this.loggedInID = loggedInID;
    }

    public String getDisplayName() {
        return displayName;
    }
    public int getContactId() {return contactId;}

    public void setContactId(int contactId) {
        this.contactId = contactId;
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

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public String getLastMessageCreated() {
        return lastMessageCreated;
    }

    public void setLastMessageCreated(String lastMessageCreated) {
        this.lastMessageCreated = lastMessageCreated;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
