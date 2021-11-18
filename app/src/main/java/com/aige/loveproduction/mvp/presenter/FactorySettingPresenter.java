package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.HandlerBean;
import com.aige.loveproduction.bean.MachineBean;
import com.aige.loveproduction.bean.WorkgroupBean;
import com.aige.loveproduction.mvp.contract.FactorySettingContract;
import com.aige.loveproduction.mvp.model.FactorySettingModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class FactorySettingPresenter extends BasePresenter<FactorySettingContract.View,FactorySettingContract.Model> implements FactorySettingContract.Presenter {

    @Override
    public FactorySettingContract.Model bindModel() {
        return (FactorySettingContract.Model) FactorySettingModel.newInstance();
    }

    @Override
    public void getMachine(boolean isInit) {
        checkViewAttached();
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        mModel.getMachine().compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<MachineBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<List<MachineBean>> machineBeanBaseBean) {
                        mView.onGetMachineSuccess(machineBeanBaseBean, isInit);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideLoading(methodName);
                        analysisThrowable(e,methodName);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading(methodName);
                    }
                });

    }

    @Override
    public void getWorkgroupByMachineId(String machineId, boolean isInit) {
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        checkViewAttached();
        mModel.getWorkgroupByMachineId(machineId).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<WorkgroupBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<List<WorkgroupBean>> workgroupBeanBaseBean) {
                        mView.onGetWorkgroupSuccess(workgroupBeanBaseBean, isInit);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideLoading(methodName);
                        analysisThrowable(e,methodName);
                    }
                    @Override
                    public void onComplete() {
                        mView.hideLoading(methodName);
                    }
                });

    }

    @Override
    public void getHandlerByWorkgroupId(String workgroupId, boolean isInit) {
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        if (!isViewAttached()) {
            return;
        }
        mModel.getHandlerByWorkgroupId(workgroupId).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<HandlerBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading(methodName);
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<List<HandlerBean>> handlerBeanBaseBean) {
                        mView.onGetHandlerSuccess(handlerBeanBaseBean, isInit);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideLoading(methodName);
                        analysisThrowable(e,methodName);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading(methodName);
                    }
                });

    }

    //@Override
//    private void analysisThrowable(Throwable e,String methodName) {
//        if(e instanceof SocketTimeoutException) {
//            mView.onError(methodName, "获取工作组超时");
//        }else if(e instanceof HttpException) {
//            HttpException exception = (HttpException) e;
//            if(exception.code() == 500) mView.onError("找不到数据");
//        }else if(e instanceof ConnectException){
//            mView.onError(methodName,"请连接网络重试");
//        }else{
//            mView.onError(methodName,e.getMessage());
//        }
//    }
}
