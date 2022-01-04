package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.PlanNoScanContract;
import com.aige.loveproduction.mvp.model.PlanNoScanModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlanNoScanPresenter extends BasePresenter<PlanNoScanContract.View,PlanNoScanModel> implements PlanNoScanContract.Presenter {
    private static final List<ScanCodeBean> listBeans = new ArrayList<>();
    @Override
    public PlanNoScanModel bindModel() {
        return new PlanNoScanModel();
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
    public void getWonoByBatchNo(String batchNo, String opId, WonoAsk ask) {
        checkViewAttached();
        mModel.getWonoByBatchNo(batchNo,opId,ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<TransferBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<TransferBean> response) {
                        listBeans.clear();
                        getMessageByWono(response,ask);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                    }

                    @Override
                    public void onNormalEnd() {
//                        mView.hideLoading();
                    }
                });
    }
    //聚合请求
    @Override
    public void getMessageByWono(List<TransferBean> beans,WonoAsk ask) {
        checkViewAttached();
        Observable<BaseBean<ScanCodeBean>>[] arr = new Observable[beans.size()];
        for (int i = 0; i < beans.size();i++) {
            WonoAsk wonoAsk = newAsk(ask);
            wonoAsk.setScanCode(beans.get(i).getWono());
            arr[i] = mModel.getMessageByWono(wonoAsk);
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
                        listBeans.add(response);
                        if(response.getWono().equals(beans.get(beans.size()-1).getWono())) {
                            mView.hideLoading();
                            mView.onGetMessageByWonoSuccess(listBeans);
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
