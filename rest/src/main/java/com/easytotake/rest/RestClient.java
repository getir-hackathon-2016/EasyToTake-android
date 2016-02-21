package com.easytotake.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.easytotake.rest.interceptors.LoggingInterceptor;
import com.easytotake.rest.util.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class RestClient {
    private static Backend service;

    public static void init() {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        OkHttpClient client = new OkHttpClient();
        client.setCookieHandler(cookieManager);
        client.networkInterceptors().add(new LoggingInterceptor());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.Rest.BASE_URL)
                .client(client)
                .build();

        service = retrofit.create(Backend.class);
    }

    public static Backend getService() {
        return service;
    }


}
