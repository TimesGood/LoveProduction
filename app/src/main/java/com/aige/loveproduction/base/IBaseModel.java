package com.aige.loveproduction.base;

import com.aige.loveproduction.net.APIService;
import com.aige.loveproduction.net.RetrofitClient;
import com.google.gson.Gson;

/**
 * M层接口
 * 主要用于网络请求，获取网络请求的数据
 */
public interface IBaseModel {
    Gson gson = new Gson();
    /**
     * 获取网络请求Api实例
     * @return APIService实例对象
     */
    default APIService getApi() {
       return RetrofitClient.getInstance().getApi();
    }
    default Gson getGson() {
        return new Gson();
    }

}