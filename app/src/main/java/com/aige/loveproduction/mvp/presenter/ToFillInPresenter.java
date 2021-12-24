package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.bean.ToFillInAsk;
import com.aige.loveproduction.bean.ToFillInBean;
import com.aige.loveproduction.mvp.contract.CreateTaskContract;
import com.aige.loveproduction.mvp.contract.ToFillInContract;
import com.aige.loveproduction.mvp.model.CreateTaskModel;
import com.aige.loveproduction.mvp.model.ToFillInModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import io.reactivex.rxjava3.disposables.Disposable;

public class ToFillInPresenter extends BasePresenter<ToFillInContract.View,ToFillInModel> implements ToFillInContract.Presenter{
    @Override
    public ToFillInModel bindModel() {
        return new ToFillInModel();
    }

    @Override
    public void getToFillInData(String barcode) {
        checkViewAttached();
        mModel.getToFillInData(barcode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<ToFillInBean>() {
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(ToFillInBean response) {
                        mView.onGetToFillInDataSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void submitData(ToFillInAsk ask) {
        checkViewAttached();
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        mModel.submitData(ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onSuccess(Object response) {
                        mView.onSubmitDataSuccess();
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(methodName,message);
                        mView.hideLoading(methodName);
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading(methodName);
                    }
                });

    }
}
