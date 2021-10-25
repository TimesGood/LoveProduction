package com.aige.loveproduction.presenter;


import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.contract.StorageFindContract;
import com.aige.loveproduction.model.StorageFindModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class StorageFindPresenter extends BasePresenter<StorageFindContract.View,StorageFindContract.Model> implements StorageFindContract.Presenter {

    @Override
    public StorageFindContract.Model bindModel() {
        return (StorageFindContract.Model) StorageFindModel.newInstance();
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
}
