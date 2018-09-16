package com.jasonzou.retrofitdemo.network;

import com.jasonzou.retrofitdemo.BaseApplication;
import com.jasonzou.retrofitdemo.BuildConfig;
import com.jasonzou.retrofitdemo.util.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 针对指定服务器的配置
 */
public class APIMaster {

    private static APIMaster mInstance;
    private static Retrofit retrofit;
    private static volatile API api = null;

    private APIMaster() { }

    public void init(BaseApplication application) {

        //拦截器-公有参数
        CommonHeadersInterceptor headersInterceptor = new CommonHeadersInterceptor.Builder()
                .addHeaderParams("paltform","android")
                .addHeaderParams("version",BuildConfig.VERSION_NAME)
                .addHeaderParams("User-Agent", Utils.getUserAgent(application))//【浏览器标识】
                .build();

        // 初始化okhttp
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(headersInterceptor)
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(5,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(httpClient)
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
