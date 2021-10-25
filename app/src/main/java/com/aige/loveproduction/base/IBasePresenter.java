package com.aige.loveproduction.base;

import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.HttpException;

/**
 * P层接口
 * @param <V>
 */
public interface IBasePresenter<V extends IBaseView> {

    void onAttach(V v);

    void onDetach();
    
}