package com.aige.loveproduction.presenter;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.contract.PlateFindContract;
import com.aige.loveproduction.model.PlateFindModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class PlateFindPresenter extends BasePresenter<PlateFindContract.View,PlateFindContract.Model> implements PlateFindContract.Presenter {

    @Override
    public PlateFindContract.Model bindModel() {
        return (PlateFindContract.Model) PlateFindModel.newInstance();
    }

    @Override
    public void getPlateListByPackageCode(String barcode) {
        checkViewAttached();
        mModel.getPlateListByPackageCode(barcode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<PlateBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<List<PlateBean>> listBaseBean) {
                        mView.onGetPlateListByPackageCodeSuccess(listBaseBean);
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
