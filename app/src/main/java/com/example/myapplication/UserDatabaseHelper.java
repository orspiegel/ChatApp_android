package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "Users";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";
    private static final String COL_4 = "DISPLAYNAME";
    private static final String COL_5 = "PROFILEPIC";
    private static final String COL_6 = "CONTACTLIST";
    private static final String COL_7 = "ISAUTHENTICATED";
    private Gson gson = new Gson();

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT,PASSWORD TEXT,DISPLAYNAME TEXT,PROFILEPIC TEXT,CONTACTLIST TEXT,ISAUTHENTICATED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertData(String username, String password, String displayName, String profilePic, ArrayList<String> contactList, boolean isAuthenticated) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        contentValues.put(COL_4, displayName);
        contentValues.put(COL_5, profilePic);
        Gson gson = new Gson();
        String contactListString = gson.toJson(contactList);
        contentValues.put(COL_6, contactListString);
        contentValues.put(COL_7, isAuthenticated ? 1 : 0);
        long newRowId = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        return newRowId;
    }

    public boolean updateIsAuthenticated(String username, boolean isAuthenticated) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, isAuthenticated ? 1 : 0);
        db.update(TABLE_NAME, contentValues, "USERNAME = ?", new String[]{username});
        return true;
    }

    public Cursor getUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME=?", new String[]{username});
        return res;
    }

    public ArrayList<String> getContactList(String contactListJson) {
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(contactListJson, type);
    }

    public boolean deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        return true;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select id, username, password, isAuthenticated, contactList from " + TABLE_NAME, null);
        return res;
    }
    public void updateUserAuthentication(String username, boolean isAuthenticated) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("isAuthenticated", isAuthenticated ? 1 : 0);
        String selection = "username" + " = ?";
        String[] selectionArgs = {username};

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}