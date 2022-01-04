package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.SpecialShapedContract;
import com.aige.loveproduction.mvp.contract.TransfersContract;
import com.aige.loveproduction.mvp.model.SpecialShapedModel;
import com.aige.loveproduction.mvp.model.TransfersModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class TransfersPresenter extends BasePresenter<TransfersContract.View, TransfersModel> implements TransfersContract.Presenter {
    @Override
    public TransfersModel bindModel() {
        return new TransfersModel();
    }

    /**
     * 扫描获取列表
     */
    @Override
    public void getTransportVerification(String packageCode) {
        checkViewAttached();
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        mModel.getTransportVerification(packageCode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<TransportBean>() {
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                    }

                    @Override
                    public void onSuccess(TransportBean response) {
                        mView.onGetTransport(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(methodName,message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    /**
     * 扫描扫描列表上的包装
     */
    @Override
    public void transportScan(String packageCode, String transportName) {
        checkViewAttached();
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        mModel.transportScan(packageCode,transportName).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        setDisposable(d);
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean baseBean) {
                        mView.onScanSuccess(baseBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onError(methodName,e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 提交
     */
    @Override
    public void transportSubmit(String packageCode) {
        checkViewAttached();
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        mModel.transportSubmit(packageCode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                    }

                    @Override
                    public void onSuccess(Object response) {
                        mView.onSubmitSuccess(null);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(methodName,message);
                    }

                    @Override
                    public void onNormalEnd() {
                    }
                });
    }
}
