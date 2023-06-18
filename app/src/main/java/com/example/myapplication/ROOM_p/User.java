package com.example.myapplication.ROOM_p;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
   @PrimaryKey(autoGenerate=true)
   private int id;
   private String userName;
   private String image;
   private String displayName;
   private String UserId;


   public User(String userName, String image, String displayName, String userId) {
      this.userName = userName;
      this.image = image;
      this.displayName = displayName;
      this.UserId = userId;
   }

   public User() {
   }
   public void setId(int id) {
      this.id = id;
   }

   public String getUserId() {
      return UserId;
   }

   public void setUserId(String userId) {
      UserId = userId;
   }
   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }

   public String getDisplayName() {
      return displayName;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public int getId() {
      return id;
   }

   public void setId(String id) {
      this.UserId = id;
   }
}
