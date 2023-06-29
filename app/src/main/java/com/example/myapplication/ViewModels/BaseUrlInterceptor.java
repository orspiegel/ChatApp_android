package com.example.myapplication.ViewModels;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseUrlInterceptor implements Interceptor {
    private volatile String baseUrl;
    private static BaseUrlInterceptor instance;

    private BaseUrlInterceptor() {
    }

    public static synchronized BaseUrlInterceptor getInstance() {
        if (instance == null) {
            instance = new BaseUrlInterceptor();
        }
        return instance;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl oldUrl = originalRequest.url();

        // Check if baseUrl is null
        if (baseUrl != null) {
            HttpUrl newUrl = HttpUrl.parse(baseUrl);
            HttpUrl.Builder newUrlBuilder = oldUrl.newBuilder()
                    .scheme(newUrl.scheme())
                    .host(newUrl.host())
                    .port(newUrl.port());

            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .method(originalRequest.method(), originalRequest.body())
                    .url(newUrlBuilder.build());

            return chain.proceed(requestBuilder.build());
        } else {
            throw new NullPointerException("BaseUrlInterceptor: BaseUrl is null, call setBaseUrl(String baseUrl) before making requests");
        }
    }

}
