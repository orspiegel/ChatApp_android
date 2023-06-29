package com.example.myapplication.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.example.myapplication.ChatListActivity;
import com.example.myapplication.Dao.UserDao;
import com.example.myapplication.Entites.User;
import com.example.myapplication.Entites.UserRegistration;
import com.example.myapplication.MyApplication;
import com.example.myapplication.Objects.TokenRequest;
import com.example.myapplication.R;
import com.example.myapplication.State.LoggedUser;
//import com.example.myapplication.ViewModels.BaseUrlInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class UserAPI {
    private final Retrofit retrofit;
    private final WebServiceAPI webServiceAPI;
    private final UserDao userDao;

    public UserAPI(UserDao userDao) {
        String url = MyApplication.context.getString(R.string.baseUrl);

        /*OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(BaseUrlInterceptor.getInstance())
                .build();*/

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.userDao = userDao;
    }

    public void addUser(User user) {
        UserRegistration userRegistration = new UserRegistration(
                user.getUserName(),
                user.getPassword(),
                user.getDisplayName(),
                user.getProfilePic()
        );
        Call<Void> call = webServiceAPI.register(userRegistration);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "User added successfully.");
                } else {
                    Log.d("UserAPI", "Failed to add user. Response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("UserAPI", "Error: " + t.getMessage());
            }
        });
    }


    public void logAllUsers() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.getAllUsers().observeForever(new Observer<List<User>>() {
                    @Override
                    public void onChanged(List<User> users) {
                        for (User user : users) {
                            Log.d("UserDB", "User: " + user.toString());
                        }
                        userDao.getAllUsers().removeObserver(this);
                    }
                });
            }
        });
    }
    public void login(String username, String password, String token) {
        Call<User> call = webServiceAPI.getUserInfo("bearer " + token, username);
        Log.d("login function", "input token: "+token+" username: "+username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("UserApi", "Response: "+response);
                User user = response.body();
                Log.d("UserApi", "Response body: "+user);
                Log.d("UserApi", "In login");
                LoggedUser.setLoggedIn(user.getDisplayName(), user.getProfilePic());
                Intent intent = new Intent(MyApplication.context, ChatListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.context.startActivity(intent);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("UserApi", "Error: "+t);
            }
        });
    }


    public void getToken(String username, String password, TokenCallback callback) {
        TokenRequest tokenRequest = new TokenRequest(username, password);
        Call<ResponseBody> call = webServiceAPI.getToken(tokenRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    String token = null;
                    try {
                        token = responseBody.string();
                        MyApplication.setToken(token);
                        callback.onTokenReceived(token);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("UserApi", "------------------------Token----------------------------");
                    Log.d("UserApi", "Token: " + token);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UserApi", "Token request failed", t);
                callback.onTokenReceived(null);
            }
        });
    }
    public void login(String username, String password, String token, TokenCallback callback) {
        Call<User> call = webServiceAPI.getUserInfo("bearer " + token, username);
        Log.d("login function", "input token: "+token+" username: "+username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d("UserApi", "Response: "+response);
                User user = response.body();
                Log.d("UserApi", "Response body: "+user.getDisplayName());
//                Log.d("UserApi", "Response body: "+user.getProfilePic());

                Log.d("UserApi", "In login");
                LoggedUser.setLoggedIn(user.getDisplayName(), user.getProfilePic());
                callback.onLoginSuccess(MyApplication.context, ChatListActivity.class);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("UserApi", "Error: "+t);
            }
        });
    }
    public interface TokenCallback {
        void onTokenReceived(String token);

        void onLoginSuccess(Context context, Class<ChatListActivity> chatListActivityClass);
    }

}
