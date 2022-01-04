package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.TransferContract;
import com.aige.loveproduction.mvp.model.TransferModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class TransferPresenter extends BasePresenter<TransferContract.View,TransferModel> implements TransferContract.Presenter {
    private static final List<ScanCodeBean> scanCodeBeanList = new ArrayList<>();

    @Override
    public TransferModel bindModel() {
        return new TransferModel();
    }
    private WonoAsk newAsk(WonoAsk ask) {
        WonoAsk newAsk = new WonoAsk();
        newAsk.setScanCode(ask.getScanCode());
        newAsk.setOperationType(ask.getOperationType());
        newAsk.setEmployeeId(ask.getEmployeeId());
        newAsk.setUserName(ask.getUserName());
        newAsk.setWorkGroupId(ask.getWorkGroupId());
        newAsk.setMachineId(ask.getMachineId());
        newAsk.setOperationId(ask.getOperationId());
        return newAsk;
    }
    @Override
    public void getWonoByPackageCode(String packageCode, String operationEntity_Id,WonoAsk ask) {
        checkViewAttached();
        mModel.getWonoByPackageCode(packageCode,operationEntity_Id,ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<TransferBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<TransferBean> response) {
                        scanCodeBeanList.clear();
                        getMessageByWono(response,ask);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {

                    }
                });
    }

    @Override
    public void getMessageByWono(List<TransferBean> beans ,WonoAsk ask) {
        checkViewAttached();
        Observable<BaseBean<ScanCodeBean>>[] arr = new Observable[beans.size()];
        for (int i = 0;i < beans.size();i++) {
            WonoAsk wonoAsk = newAsk(ask);
            wonoAsk.setScanCode(beans.get(i).getWono());
            arr[i] = mModel.getMessageByWono(ask);
        }
        Observable.concatArray(arr).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<ScanCodeBean>() {
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                    }

                    @Override
                    public void onSuccess(ScanCodeBean response) {
                        if(response.getCode() == 0) {
                            response.setMessage("扫描成功");
                        }else{
                            response.setMessage(response.getMsg());
                        }
                        scanCodeBeanList.add(response);
                        if(response.getWono().equals(beans.get(beans.size()-1).getWono())) {
                            mView.hideLoading();
                            mView.onGetMessageByWonoSuccess(scanCodeBeanList);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {

                    }
                });

    }

}
