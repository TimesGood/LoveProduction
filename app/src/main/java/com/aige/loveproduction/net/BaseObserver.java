package com.aige.loveproduction.net;


import com.aige.loveproduction.base.BaseBean;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 对网络请求结果进行处理，把code非0的都当做异常处理
 * 接口统一一下吧，某些接口code非0但还是要数据回去，这就烦人了
 */
public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        onStart(d);
    }
    @Override
    public void onNext(@NonNull BaseBean<T> response) {
        //返回数据状态为0时，才返回数据，否则以异常处理
        if(response.getCode() == 0) {
//            T data = response.getData();
//            if(data == null || "[]".equals(data.toString())) {
//                onError("空数据");
//            }
            onSuccess(response.getData());
        }else{
            onError(response.getMsg());
        }
    }
    @Override
    public void onError(@NonNull Throwable e) {
        if(e instanceof SocketTimeoutException) {
            onError("请求超时");
        }else if(e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            if(exception.code() >= 400 && exception.code() < 500) {
                onError("服务器错误");
            }else if(exception.code() >= 500 && exception.code() < 600) {
                onError("服务器找不到数据");
            }else {
                onError(e.getMessage());
            }
        }else if(e instanceof ConnectException){
            onError("请连接网络后重试");
        }else{
            onError(e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        onNormalEnd();
    }

    public abstract void onStart(Disposable d);
    public abstract void onSuccess(T response);
    public abstract void onError(String message);
    public abstract void onNormalEnd();
}