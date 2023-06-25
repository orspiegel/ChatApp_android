package com.example.myapplication;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context context;
    public static String token;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        token = null;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        MyApplication.token = token;
    }
}
