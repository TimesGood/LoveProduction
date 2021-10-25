package com.aige.loveproduction.base;

import com.aige.loveproduction.model.FactorySettingModel;
import com.aige.loveproduction.net.APIService;
import com.aige.loveproduction.net.RetrofitClient;

/**
 * Model主要对网络请求结果进行封装
 * Created by haoran on 2018/4/4.
 */
public interface IBaseModel {
    /**
     * 获取网络请求Api实例
     * @return APIService实例对象
     */
    default APIService getApi() {
       return RetrofitClient.getInstance().getApi();
    }

}