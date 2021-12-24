package com.aige.loveproduction.mvp.presenter;


import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.mvp.contract.InStorageContract;
import com.aige.loveproduction.mvp.model.InStorageModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class InStoragePresenter extends BasePresenter<InStorageContract.View,InStorageModel> implements InStorageContract.Presenter {

    @Override
    public InStorageModel bindModel() {
        return new InStorageModel();
    }

    @Override
    public void getScanPackage(String packageCode, String userName, String action, String binCode) {
        checkViewAttached();
        mModel.getScanPackage(packageCode, userName, action, binCode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<StorageBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }
                    @Override
                    public void onNext(@NonNull BaseBean<List<StorageBean>> listBaseBean) {
                        mView.onGetScanPackageSuccess(listBaseBean);
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
    public void getBinFind(String binCode) {
        checkViewAttached();
        mModel.getBinFind(binCode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<BinFindBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<BinFindBean> binFindBeanBaseBean) {
                        mView.onGetBinFindSuccess(binFindBeanBaseBean);
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
