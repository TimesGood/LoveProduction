package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.mvp.contract.CreateTaskContract;
import com.aige.loveproduction.mvp.model.CreateTaskModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import io.reactivex.rxjava3.disposables.Disposable;

public class CreateTaskPresenter extends BasePresenter<CreateTaskContract.View,CreateTaskContract.Model> implements CreateTaskContract.Presenter{
    @Override
    public CreateTaskContract.Model bindModel() {
        return (CreateTaskContract.Model) CreateTaskModel.newInstance();
    }

    @Override
    public void getEntityByBarcode(String barcode) {
        checkViewAttached();
        mModel.getEntityByBarcode(barcode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<PrintBean>() {
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(PrintBean response) {
                        mView.onGetEntityByBarcodeSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void submitPrint(PrintAsk ask) {
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        checkViewAttached();
        mModel.submitPrint(ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onSuccess(Object response) {
                        mView.onSubmitPrintSuccess();
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(methodName,message);
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading(methodName);
                    }
                });
    }
}
