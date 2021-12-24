package com.aige.loveproduction.net;


import com.aige.loveproduction.BuildConfig;
import com.aige.loveproduction.net.interceptor.BaseUrlInterceptor;
import com.aige.loveproduction.net.interceptor.HeaderInterceptor;
import com.aige.loveproduction.net.interceptor.TimeoutInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit的配置、初始化、创建实例
 */
public class RetrofitClient {
    private static volatile RetrofitClient instance;
    private APIService apiService;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {
    }

    /**
     * 创建单一实例，对该对象上锁，防止多线程创建多个对象
     */
    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }
    /**
     * 创建OkHttp实例
     */
    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            //对不同环境进行处理
            if (BuildConfig.DEBUG) {
                okHttpClient = new OkHttpClient().newBuilder()
                        //默认重试一次，若需要重试N次，则要实现拦截器
                        .retryOnConnectionFailure(true)
                        //请求头拦截
                        .addInterceptor(new HeaderInterceptor())
                        .addInterceptor(new BaseUrlInterceptor())
                        .addInterceptor(new TimeoutInterceptor())
                        //日志拦截，设置打印日志的级别
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        //设置dns
                        .dns(new ApiDns())
                        .build();
            } else {
                okHttpClient = new OkHttpClient().newBuilder()
                        .retryOnConnectionFailure(true)
                        .addInterceptor(new HeaderInterceptor())
                        .addInterceptor(new BaseUrlInterceptor())
                        .addInterceptor(new TimeoutInterceptor())
                        .dns(new ApiDns())
                        .build();
            }
        }
        return okHttpClient;
    }
    /**
     * 创建Retrofit实例与网络接口实例
     */
    public APIService getApi() {
        //创建Retrofit实例
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //设置网络请求的Url地址
                    .baseUrl(ApiConstants.base_url)
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        //创建网络请求接口实例
        if (apiService == null) {
            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }
}
