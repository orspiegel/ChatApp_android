package com.example.myapplication.DB;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.example.myapplication.Objects.MessageItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@ProvidedTypeConverter
public class MessagesConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<MessageItem> fromStringToMessageItemList(String messages) {

        if (messages == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<MessageItem>>() {}.getType();

        return (gson.fromJson(messages, listType));
    }

    @TypeConverter
    public static String fromMessageItemListToString(List<MessageItem> mL) {

        return gson.toJson(mL);
    }
}
