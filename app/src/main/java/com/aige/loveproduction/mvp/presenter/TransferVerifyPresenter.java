package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.TransferVerifyContract;
import com.aige.loveproduction.mvp.contract.TransfersContract;
import com.aige.loveproduction.mvp.model.TransferVerifyModel;
import com.aige.loveproduction.mvp.model.TransfersModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class TransferVerifyPresenter extends BasePresenter<TransferVerifyContract.View, TransferVerifyModel> implements TransferVerifyContract.Presenter {
    @Override
    public TransferVerifyModel bindModel() {
        return new TransferVerifyModel();
    }
    @Override
    public void getTransportVerification(String packageCode) {
        checkViewAttached();
        mModel.getTransportVerification(packageCode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<TransportBean>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                        setDisposable(d);
                    }

                    @Override
                    public void onSuccess(TransportBean response) {
                        mView.onGetTransport(response);
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

}
