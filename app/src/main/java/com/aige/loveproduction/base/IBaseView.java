package com.aige.loveproduction.base;

import autodispose2.AutoDisposeConverter;

/**
 * V层基类
 */
public interface IBaseView {


    void showLoading();

    void hideLoading();

    default void onError(String message) {}

    default void showLoading(String method) {}
    default void hideLoading(String method) {}
    default void onError(String method,String message) {}

    <T> AutoDisposeConverter<T> bindAutoDispose();

}