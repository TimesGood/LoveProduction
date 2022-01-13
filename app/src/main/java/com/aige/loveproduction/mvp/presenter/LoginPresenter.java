package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.UserBean;
import com.aige.loveproduction.mvp.contract.LoginContract;
import com.aige.loveproduction.mvp.model.LoginModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class LoginPresenter extends BasePresenter<LoginContract.View,LoginModel> implements LoginContract.Presenter {

    @Override
    public LoginModel bindModel() {
        return new LoginModel();
    }

    @Override
    public void login(String username, String password) {
        checkViewAttached();
        mModel.login(username, password).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<UserBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<UserBean> bean) {
                        mView.onLoginSuccess(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        analysisThrowable(e);
                        mView.hideLoading();

                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
    @Override
    public void analysisThrowable(Throwable e){
        if(e instanceof SocketTimeoutException) {
            mView.onError( "登录超时");
        }else if(e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            if(exception.code() == 500) mView.onError("没有该用户");
        }else if(e instanceof ConnectException){
            mView.onError("请连接网络后重试");
        }else{
            mView.onError(e.getMessage());
        }
    }
}
