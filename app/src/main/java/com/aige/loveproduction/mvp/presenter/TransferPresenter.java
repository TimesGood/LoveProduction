package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.TransferContract;
import com.aige.loveproduction.mvp.model.TransferModel;
import com.aige.loveproduction.net.RxScheduler;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class TransferPresenter extends BasePresenter<TransferContract.View,TransferContract.Model> implements TransferContract.Presenter {
    private List<ScanCodeBean> scanCodeBeanList;
    private BaseBean<List<ScanCodeBean>> scanCodeBean;
    //记录请求的数据列表数量
    private int wonoSize = 0;
    //执行次数
    private int execute = 0;

    @Override
    public TransferContract.Model bindModel() {
        return (TransferContract.Model) TransferModel.newInatance();
    }

    @Override
    public void getWonoByPackageCode(String packageCode, String operationEntity_Id,WonoAsk ask) {
        checkViewAttached();
        mModel.getWonoByPackageCode(packageCode,operationEntity_Id,ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<TransferBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }
                    @Override
                    public void onNext(@NonNull BaseBean<List<TransferBean>> transferBeanBaseBean) {

                        scanCodeBeanList = new ArrayList<>();
                        scanCodeBean = new BaseBean<>();
                        execute = 0;
                        if(transferBeanBaseBean.getCode() == 0) {
                            mView.onGetWonoByPackageCodeSuccess(transferBeanBaseBean);
                            List<TransferBean> data = transferBeanBaseBean.getData();
                            wonoSize = data.size();
                            for(TransferBean bean : data) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ask.setScanCode(bean.getWono());
                                getMessageByWono(ask);
                            }
                        }else{
                            mView.hideLoading();
                            mView.onError(transferBeanBaseBean.getMsg());
                        }


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.hideLoading();
                        analysisThrowable(e);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getMessageByWono(WonoAsk ask) {
        checkViewAttached();
        mModel.getMessageByWono(ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<PlanNoMessageBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }
                    @Override
                    public void onNext(@NonNull BaseBean<PlanNoMessageBean> bean) {
                        execute++;
                        if(bean.getCode() == 0) {
                            PlanNoMessageBean data = bean.getData();
                            ScanCodeBean scanCodeBean = new ScanCodeBean();
                            scanCodeBean.setWono(data.getWono());
                            scanCodeBean.setTotalArea(String.valueOf(data.getTotalArea()));
                            scanCodeBean.setOrderId(data.getOrderId());
                            if(data.getCode() == 0) {
                                scanCodeBean.setMessage("扫描成功");
                            }else{
                                scanCodeBean.setMessage(data.getMsg());
                            }
                            scanCodeBean.setPlanNo(data.getPlanNo());
                            scanCodeBean.setTotalCnt(String.valueOf(data.getTotalCnt()));
                            scanCodeBeanList.add(scanCodeBean);
                        }else{
                            ScanCodeBean scanCodeBean = new ScanCodeBean();
                            scanCodeBean.setWono(ask.getScanCode());
                            scanCodeBean.setMessage("异常");
                            scanCodeBeanList.add(scanCodeBean);
                        }
                        if(scanCodeBeanList.size() == wonoSize) {
                            scanCodeBean.setData(scanCodeBeanList);
                            mView.onGetMessageByWonoSuccess(scanCodeBean);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        execute++;
                        if(wonoSize == execute) {
                            ScanCodeBean scanCodeBean = new ScanCodeBean();
                            scanCodeBean.setWono(ask.getScanCode());
                            scanCodeBean.setMessage("异常");
                            scanCodeBeanList.add(scanCodeBean);
                        }
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });



    }

}
