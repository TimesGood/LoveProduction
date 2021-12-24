package com.aige.loveproduction.base;

import autodispose2.AutoDisposeConverter;

/**
 * V层接口
 * 主要用于请求网络前加载与加载完毕的操作
 */
public interface IBaseView {

    //显示加载
    void showLoading();
    //隐藏加载
    void hideLoading();
    //请求错误
    default void onError(String message) {}


    default void showLoading(String method) {}
    default void hideLoading(String method) {}
    default void onError(String method,String message) {}

    //解绑
    <T> AutoDisposeConverter<T> bindAutoDispose();

}