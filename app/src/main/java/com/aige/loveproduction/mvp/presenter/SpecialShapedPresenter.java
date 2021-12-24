package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.SpeciaBarAsk;
import com.aige.loveproduction.mvp.contract.SpecialShapedContract;
import com.aige.loveproduction.mvp.model.SpecialShapedModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class SpecialShapedPresenter extends BasePresenter<SpecialShapedContract.View,SpecialShapedModel> implements SpecialShapedContract.Presenter {
    @Override
    public SpecialShapedModel bindModel() {
        return new SpecialShapedModel();
    }

    public void getPlateListByPackageCode(String barcode) {
        checkViewAttached();
        mModel.getPlateByPackageCode(barcode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<PlateBean>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(PlateBean response) {
                        mView.onGetPlateListByPackageCodeSuccess(response);
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
    public void getSpecialBar(SpeciaBarAsk ask) {
        checkViewAttached();
        mModel.getSpecialBar(ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.submitShowLoading();
                    }

                    @Override
                    public void onSuccess(Object response) {
                        mView.onGetSpecialBar(null);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.submitHideLoading();
                    }
                });
    }
}
