package com.aige.loveproduction.net.interceptor;

import com.aige.loveproduction.net.HostType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 当有需要在一个接口上设置超时时间时，拦截并把设置的超时时间设置
 */
public class TimeoutInterceptor implements Interceptor{
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        int connectTimeout = chain.connectTimeoutMillis();
        int readTimeout = chain.readTimeoutMillis();
        int writeTimeout = chain.writeTimeoutMillis();
        String connectNew = request.header(HostType.CONNECT_TIMEOUT);
        String readNew = request.header(HostType.READ_TIMEOUT);
        String writeNew = request.header(HostType.WRITE_TIMEOUT);
        if(connectNew != null) connectTimeout = Integer.parseInt(connectNew);
        if(readNew != null) readTimeout = Integer.parseInt(readNew);
        if(writeNew != null) writeTimeout = Integer.parseInt(writeNew);
        return chain
                .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
                .withReadTimeout(readTimeout, TimeUnit.SECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
                .proceed(request);
    }
}
