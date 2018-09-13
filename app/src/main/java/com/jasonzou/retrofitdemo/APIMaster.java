package com.jasonzou.retrofitdemo;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIMaster {
    static Retrofit retrofit;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(API.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static API getAPI() {
        if (retrofit != null) {
            return retrofit.create(API.class);
        }else {
            return null;
        }
    }
}
