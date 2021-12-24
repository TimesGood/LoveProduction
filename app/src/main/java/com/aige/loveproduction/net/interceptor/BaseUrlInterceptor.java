package com.aige.loveproduction.net.interceptor;
import com.aige.loveproduction.net.HostType;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拦截请求，当头部有更换服务器地址的意愿时，把默认地址替换
 */
public class BaseUrlInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl old_url = request.url();//获取原HttpUrl
        String base_url = request.header(HostType.BASE_URL);//获取头部配置的服务器地址
        if(base_url != null) {
            Request.Builder builder = request.newBuilder();
            builder.removeHeader(HostType.BASE_URL);
            HttpUrl new_base_url = HttpUrl.parse(base_url);
            //重建新的HttpUrl，修改需要修改的地方
            HttpUrl new_url = old_url
                    .newBuilder()
                    .scheme(new_base_url.scheme())
                    .host(new_base_url.host())
                    .port(new_base_url.port())
                    .build();
            return chain.proceed(builder.url(new_url).build());

        }
        return chain.proceed(request);
    }
}
