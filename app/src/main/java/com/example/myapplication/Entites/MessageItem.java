package com.example.myapplication.Entites;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_item_table",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "username",
                childColumns = "senderUserName",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("senderUserName")})
public class MessageItem {

    @PrimaryKey
    @NonNull
    private String msgID;

    private String content;
    private String created;

    @ColumnInfo(name = "senderUserName")
    private String senderUserName;

}
