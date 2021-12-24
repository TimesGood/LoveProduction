package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.PlateWrapBean;
import com.aige.loveproduction.mvp.contract.PlateFindContract;
import com.aige.loveproduction.mvp.model.PlateFindModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlateFindPresenter extends BasePresenter<PlateFindContract.View,PlateFindModel> implements PlateFindContract.Presenter {

    @Override
    public PlateFindModel bindModel() {
        return new PlateFindModel();
    }

    @Override
    public void getPlateListByPackageCode(String barcode) {
        checkViewAttached();
        mModel.getPlateListByPackageCode(barcode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<PlateWrapBean>() {

                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(PlateWrapBean response) {
                        mView.onGetPlateListByPackageCodeSuccess(response);
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
}
