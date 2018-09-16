package com.jasonzou.retrofitdemo.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 项目:  RetrofitDemo <br>
 * 类名:  com.jasonzou.retrofitdemo.network.CommonHeadersInterceptor<br>
 * 描述:  <br/>拦截器
 * <br/>向请求头里添加公共参数：GET请求除外<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/14 17:01<br>
 */
public class CommonHeadersInterceptor implements Interceptor {
    private final Map<String, String> mHeaderParamsMap;

    private CommonHeadersInterceptor(Map<String, String> mHeaderParamsMap) {
        this.mHeaderParamsMap = mHeaderParamsMap;
    }

    private CommonHeadersInterceptor() {
        this(null);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        // 添加新的参数，添加到url 中
        /*HttpUrl.Builder authorizedUrlBuilder = originalRequest.url().newBuilder()
        .scheme(originalRequest.url().scheme())
        .host(originalRequest.url().host());*/

        // 新的请求
        Request.Builder requestBuilder = originalRequest.newBuilder();
        requestBuilder.method(originalRequest.method(), originalRequest.body());

        //添加公共参数,添加到header中
        if (mHeaderParamsMap.size() > 0) {
            for (Map.Entry<String, String> params : mHeaderParamsMap.entrySet()) {
                requestBuilder.header(params.getKey(), params.getValue());
            }
        }
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

    public static class Builder {
        private Map<String, String> mHeaderParamsMap = new HashMap<>();

        public Builder addHeaderParams(String key, String value) {
            mHeaderParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParams(String key, int value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, float value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, long value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, double value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public CommonHeadersInterceptor build() {
            return new CommonHeadersInterceptor(mHeaderParamsMap);
        }
    }
}