package com.example.myapplication.api;


import com.example.myapplication.Entites.Message;
import com.example.myapplication.Entites.User;
import com.example.myapplication.Entites.UserRegistration;
import com.example.myapplication.Objects.AddContactRequest;
import com.example.myapplication.Objects.AddContactResponse;
import com.example.myapplication.Objects.ChatResponse;
import com.example.myapplication.Objects.MessageItem;
import com.example.myapplication.Objects.TokenRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<List<ChatResponse>> getContactsList(@Header("Authorization") String authorization);

    @GET("Chats/{id}/Messages")
    Call<List<MessageItem>> getContactMessages(@Header("Authorization") String authorization, @Path("id") String id);

    @POST("Chats")
    Call<AddContactResponse> addContact(@Header("Authorization") String authorization, @Body AddContactRequest addContactRequest);

//    @POST("Chats")
//    Call<Void> addContact(@Header("Authorization") String authorization, @Body AddContactRequest addContactRequest);

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
