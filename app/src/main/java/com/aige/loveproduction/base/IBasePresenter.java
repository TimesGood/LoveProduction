package com.aige.loveproduction.base;

import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.HttpException;

/**
 * P层接口
 * 主要用于View的绑定与解绑
 */
public interface IBasePresenter<V extends IBaseView> {

    //绑定
    void onAttach(V v);

    //解绑
    void onDetach();
    
}