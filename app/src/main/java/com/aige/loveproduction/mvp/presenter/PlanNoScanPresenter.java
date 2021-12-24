package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.PlanNoScanContract;
import com.aige.loveproduction.mvp.model.PlanNoScanModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class PlanNoScanPresenter extends BasePresenter<PlanNoScanContract.View,PlanNoScanModel> implements PlanNoScanContract.Presenter {
    private List<ScanCodeBean> scanCodeBeanList;
    private BaseBean<List<ScanCodeBean>> scanCodeBeans;
    //记录请求的数据列表数量
    private int wonoSize = 0;
    @Override
    public PlanNoScanModel bindModel() {
        return new PlanNoScanModel();
    }

    @Override
    public void getWonoByBatchNo(String batchNo, String opId, WonoAsk ask) {
        checkViewAttached();
        mModel.getWonoByBatchNo(batchNo,opId,ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<List<TransferBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }
                    @Override
                    public void onNext(@NonNull BaseBean<List<TransferBean>> transferBeanBaseBean) {

                        scanCodeBeanList = new ArrayList<>();
                        scanCodeBeans = new BaseBean<>();
                        if(transferBeanBaseBean.getCode() == 0) {
                            mView.onGetWonoByBatchNoSuccess(transferBeanBaseBean);
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
//        mModel.getPlanNoMessage(ask, index).doOnNext(new Consumer<BaseBean<PlanNoMessageBean>>() {
//            @Override
//            public void accept(BaseBean<PlanNoMessageBean> bean) throws Throwable {
//                if (bean.getCode() == 0) {
//                    PlanNoMessageBean message_data = bean.getData();
//                    for (int i = 0; i < wonoSize; i++) {
//                        if (temporary_data.getData().get(i).getWono().equals(message_data.getWono())) {
//                            if (message_data.getCode() == 0) {
//                                temporary_data.getData().get(i).setMessage("扫描成功");
//                            } else {
//                                flag = false;
//                                temporary_data.getData().get(i).setMessage(message_data.getMsg());
//                            }
//                            break;
//                        }
//                    }
//                } else {
//                    mView.hideLoading();
//                }
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<BaseBean<PlanNoMessageBean>>() {
//                    @Override
//                    public void accept(BaseBean<PlanNoMessageBean> bean) throws Throwable {
//                        execute++;
//                        if (bean.getCode() == 0) {
//                            if (execute == wonoSize) {
//                                if (!flag) temporary_data.setCode(3);
//                                mView.onGetPlanNoScanList(temporary_data);
//                                mView.hideLoading();
//                            }
//                        }
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Throwable {
//                        execute++;
//                        flag = false;
//                        if (throwable instanceof SocketTimeoutException) {
//                            temporary_data.getData().get(index).setMessage("扫描超时");
//                        } else {
//                            temporary_data.getData().get(index).setMessage("异常");
//                        }
//                        if (execute == wonoSize) {
//                            if (!flag) temporary_data.setCode(3);
//                            mView.onGetPlanNoScanList(temporary_data);
//                            mView.hideLoading();
//                        }
//                    }
//                });
    }

}
