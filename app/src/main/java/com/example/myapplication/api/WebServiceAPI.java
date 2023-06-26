package com.example.myapplication.api;


import com.example.myapplication.Entites.Contact;
import com.example.myapplication.Entites.Message;
import com.example.myapplication.Entites.User;
import com.example.myapplication.Entites.UserRegistration;
import com.example.myapplication.Objects.TokenRequest;
import com.google.gson.JsonArray;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    /*
    Tokens
    * */
    @POST("Tokens")
    Call<ResponseBody> getToken(@Body TokenRequest tokenRequest);

    /*
    Chats
    * */
    @GET("Chats")
    Call<JsonArray> getContactsList(@Header("Authorization") String authorization);

    @GET("Chats/{id}/Messages")
    Call<List<Message>> getContactMessages(@Path("id") String id);

    @POST("Chats")
    Call<Contact> addContact(@Body String username);

    @POST("Chats/{id}/Messages")
    Call<Message> addMessage(@Path("id") String id, @Body String msg);


    @DELETE("Chats/{id}")
    Call<Void> deleteContact(@Path("id") String id);

    /*
    Users
    * */
    @GET("Users")
    Call<List<User>> getUsersList();

    @GET("Users/{username}")
    Call<User> getUserInfo(@Header("Authorization") String authorization, @Path("username") String username);


    @POST("Users")
    Call<Void> register(@Body UserRegistration user);
}
