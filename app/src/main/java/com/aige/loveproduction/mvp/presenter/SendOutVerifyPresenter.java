package com.aige.loveproduction.mvp.presenter;


import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.mvp.contract.SendOutVerifyContract;
import com.aige.loveproduction.mvp.model.SendOutVerifyModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class SendOutVerifyPresenter extends BasePresenter<SendOutVerifyContract.View,SendOutVerifyModel> implements SendOutVerifyContract.Presenter {
    @Override
    public SendOutVerifyModel bindModel() {
        return new SendOutVerifyModel();
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
