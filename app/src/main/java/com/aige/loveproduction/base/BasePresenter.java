package com.aige.loveproduction.base;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;


/**
 * P层基类，需要持有V层、M层，主要对其解耦及绑定对应V层、M层
 * @param <V> 继承IBaseView的接口
 * @param <M> 继承IBaseModel的接口
 */
public abstract class BasePresenter<V extends IBaseView,M extends IBaseModel> implements IBasePresenter<V> {
    protected V mView;
    protected M mModel;
    /**
     * Disposable管理容器，用于解绑一些东西
     */
    protected CompositeDisposable mDisposable;

    public BasePresenter() {
        this.mModel = bindModel();
        mDisposable = new CompositeDisposable();
    }
    /**
     * RxJava订阅
     */
    public void setDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }
    /**
     * RxJava解除订阅
     */
    public void dispose() {
        if(!mDisposable.isDisposed()) mDisposable.dispose();
    }
    /**
     * 检查View是否已经绑定
     */
    public boolean isViewAttached() {
        return mView != null;
    }

    /**
     * 检查View是否已经绑定，并抛出异常
     */
    public void checkViewAttached() {
        if (!isViewAttached()) throw new RuntimeException("未注册View");
    }

    /**
     * 绑定V层
     * @param v
     */
    @Override
    public void onAttach(V v) {
        mView = v;
        if (mModel == null) {
            throw new NullPointerException("未绑定Model");
        }
    }

    /**
     * 绑定M层
     */
    public abstract M bindModel();

    /**
     * 解除绑定释放资源
     */
    @Override
    public void onDetach() {
        if (!mDisposable.isDisposed())
            mDisposable.dispose();
        mView = null;
    }


    /**
     * 对请求网络异常进行统一处理
     */
    public void analysisThrowable(Throwable e){
        if(e instanceof SocketTimeoutException) {
            mView.onError( "请求超时");
        }else if(e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            if(exception.code() >= 400 && exception.code() < 500) {
                mView.onError("服务器错误");
            } else if(exception.code() >= 500 && exception.code() < 600) {
                mView.onError("服务器找不到数据");
            } else {
                mView.onError(e.getMessage());
            }
        }else if(e instanceof ConnectException){
            mView.onError("请连接网络后重试");
        }else{
            mView.onError(e.getMessage());
        }
    }

    public void analysisThrowable(Throwable e,String methodName){
            if(e instanceof SocketTimeoutException) {
                mView.onError(methodName, "请求超时");
            }else if(e instanceof HttpException) {
                HttpException exception = (HttpException) e;
                if(exception.code() >= 400 && exception.code() < 500) {
                    mView.onError("服务器错误");
                } else if(exception.code() >= 500 && exception.code() < 600) {
                    mView.onError("服务器找不到数据");
                } else {
                    mView.onError(e.getMessage());
                }
            }else if(e instanceof ConnectException){
                mView.onError(methodName,"请连接网络后重试");
            }else{
                mView.onError(methodName,e.getMessage());
            }
    }


}
