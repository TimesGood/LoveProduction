package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.MixedLotContract;
import com.aige.loveproduction.mvp.model.MixedLotModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class MixedLotPresenter extends BasePresenter<MixedLotContract.View,MixedLotModel> implements MixedLotContract.Presenter {
    private List<ScanCodeBean> scanCodeBeanList;
    private BaseBean<List<ScanCodeBean>> scanCodeBeans;
    //记录请求的数据列表数量
    private int wonoSize = 0;
    @Override
    public MixedLotModel bindModel() {
        return new MixedLotModel();
    }

    @Override
    public void getHunPiByBatchNo(String batchNo, String opId, String solutionName, WonoAsk ask) {
        checkViewAttached();
        mModel.getHunPiByBatchNo(batchNo,opId,solutionName,ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<ScanCodeBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<ScanCodeBean> response) {
                        scanCodeBeanList = new ArrayList<>();
                        scanCodeBeans = new BaseBean<>();
                        mView.onGetHunPiByBatchNoSuccess(response);
                        wonoSize = response.size();
                        for(ScanCodeBean bean : response) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ask.setScanCode(bean.getWono());
                            getMessageByWono(ask);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        mView.hideLoading();
                        mView.onError(message);
                    }

                    @Override
                    public void onNormalEnd() {
                    }
                });
    }

    @Override
    public void getMessageByWono(WonoAsk ask) {
        checkViewAttached();
        mModel.getMessageByWono(ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<ScanCodeBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }
                    @Override
                    public void onNext(@NonNull BaseBean<ScanCodeBean> bean) {
                        if(bean.getCode() == 0) {
                            ScanCodeBean data = bean.getData();
                            if(data.getCode() == 0) {
                                data.setMessage("扫描成功");
                            }else{
                                data.setMessage(data.getMsg());
                            }

                            data.setPlanNo(data.getPlanNo());
                            data.setTotalCnt(String.valueOf(data.getTotalCnt()));
                            scanCodeBeanList.add(data);
                        }else{
                            ScanCodeBean scanCodeBean = new ScanCodeBean();
                            scanCodeBean.setWono(ask.getScanCode());
                            scanCodeBean.setMessage("异常");
                            scanCodeBeanList.add(scanCodeBean);
                        }
                        if(scanCodeBeanList.size() == wonoSize) {
                            scanCodeBeans.setData(scanCodeBeanList);
                            mView.hideLoading();
                            mView.onGetMessageByWonoSuccess(scanCodeBeans);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ScanCodeBean scanCodeBean = new ScanCodeBean();
                        scanCodeBean.setWono(ask.getScanCode());
                        scanCodeBean.setMessage("异常");
                        scanCodeBeanList.add(scanCodeBean);
                        if(scanCodeBeanList.size() == wonoSize) {
                            scanCodeBeans.setData(scanCodeBeanList);
                            mView.hideLoading();
                            mView.onGetMessageByWonoSuccess(scanCodeBeans);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
