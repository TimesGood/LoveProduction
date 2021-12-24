package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.HandlerBean;
import com.aige.loveproduction.bean.MachineBean;
import com.aige.loveproduction.bean.WorkgroupBean;
import com.aige.loveproduction.mvp.contract.FactorySettingContract;
import com.aige.loveproduction.mvp.model.FactorySettingModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class FactorySettingPresenter extends BasePresenter<FactorySettingContract.View,FactorySettingModel> implements FactorySettingContract.Presenter {

    @Override
    public FactorySettingModel bindModel() {
        return new FactorySettingModel();
    }

    @Override
    public void getMachine() {
        checkViewAttached();
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        mModel.getMachine().compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<MachineBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onSuccess(List<MachineBean> response) {
                        mView.onGetMachineSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.hideLoading(methodName);
                        mView.onError(methodName,message);

                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading(methodName);
                    }
                });

    }

    @Override
    public void getWorkgroupByMachineId(String machineId) {
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        checkViewAttached();
        mModel.getWorkgroupByMachineId(machineId).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<WorkgroupBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onSuccess(List<WorkgroupBean> response) {
                        mView.onGetWorkgroupSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.hideLoading(methodName);
                        mView.onError(methodName,message);
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading(methodName);
                    }
                });

    }

    @Override
    public void getHandlerByWorkgroupId(String workgroupId) {
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        checkViewAttached();
        mModel.getHandlerByWorkgroupId(workgroupId).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<HandlerBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onSuccess(List<HandlerBean> response) {
                        mView.onGetHandlerSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.hideLoading(methodName);
                        mView.onError(methodName,message);
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading(methodName);
                    }
                });

    }
}
