package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.mvp.contract.PlateFindContract;
import com.aige.loveproduction.mvp.model.PlateFindModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

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
