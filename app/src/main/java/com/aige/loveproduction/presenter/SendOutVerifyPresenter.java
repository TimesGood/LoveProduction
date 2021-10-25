package com.aige.loveproduction.presenter;


import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.contract.SendOutVerifyContract;
import com.aige.loveproduction.model.SendOutVerifyModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class SendOutVerifyPresenter extends BasePresenter<SendOutVerifyContract.View,SendOutVerifyContract.Model> implements SendOutVerifyContract.Presenter {
    @Override
    public SendOutVerifyContract.Model bindModel() {
        return (SendOutVerifyContract.Model) SendOutVerifyModel.newInatance();
    }

    @Override
    public void getSendOutVerify(String packageCode, String userName, String action, String binCode) {
        checkViewAttached();
        mModel.getSendOutVerify(packageCode, userName, action, binCode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<StorageBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<List<StorageBean>> listBaseBean) {
                        mView.onGetSendOutVerifySuccess(listBaseBean);
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
}
