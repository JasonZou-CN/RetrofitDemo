package com.jasonzou.retrofitdemo.network;

import com.jasonzou.retrofitdemo.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIMaster {

    private static APIMaster mInstance;
    private static Retrofit retrofit;
    private static volatile API api = null;

    private APIMaster() { }

    public void init() {

        /*公有参数*/
        CommonHeadersInterceptor interceptor = new CommonHeadersInterceptor.Builder()
                .addHeaderParams("paltform","android")
                .addHeaderParams("version",BuildConfig.VERSION_NAME)
                .build();

        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new LoggingInterceptor())
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(API.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static API getAPI() {
        if (retrofit==null) {
            throw new RuntimeException("you must call init() first");
        }
        if (api == null) {
            synchronized (API.class) {
                if (api == null) {
                    api = retrofit.create(API.class);
                }
            }
        }
        return api;
    }

    public static APIMaster getInstance() {
        if (mInstance == null) {
            synchronized (APIMaster.class) {
                if (mInstance == null) {
                    mInstance = new APIMaster();
                }
            }
        }
        return mInstance;
    }
}
