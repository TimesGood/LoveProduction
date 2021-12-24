package com.aige.loveproduction.net.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 统一请求头请求参数送方式
 */
public class HeaderInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        Request request = requestBuilder
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        return chain.proceed(request);
    }
}
