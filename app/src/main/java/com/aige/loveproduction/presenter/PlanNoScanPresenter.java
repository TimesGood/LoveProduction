package com.aige.loveproduction.presenter;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.contract.PlanNoScanContract;
import com.aige.loveproduction.model.PlanNoScanModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import retrofit2.HttpException;

public class PlanNoScanPresenter extends BasePresenter<PlanNoScanContract.View,PlanNoScanContract.Model> implements PlanNoScanContract.Presenter {
//    //记录请求的数据列表数量
//    private int wonoSize = 0;
//    //执行次数
//    private int execute = 0;
//
//    //状态
//    private boolean flag = true;
//    //临时储存列表中的数据
//    private BaseBean<List<ScanCodeBean>> temporary_data;
    private List<ScanCodeBean> scanCodeBeanList;
    private BaseBean<List<ScanCodeBean>> scanCodeBeans;
    //记录请求的数据列表数量
    private int wonoSize = 0;
    @Override
    public PlanNoScanContract.Model bindModel() {
        return (PlanNoScanContract.Model) PlanNoScanModel.newInstance();
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
//        mModel.getPlanNoScanList(batchNo, opId, ask).compose(RxScheduler.Obs_io_main())
//                .to(mView.bindAutoDispose())
//                .subscribe(new Observer<BaseBean<List<ScanCodeBean>>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        mView.showLoading();
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseBean<List<ScanCodeBean>> listBaseBean) {
//                        if (listBaseBean.getCode() == 0) {
//                            temporary_data = listBaseBean;
//                            List<ScanCodeBean> data = temporary_data.getData();
//                            wonoSize = data.size();
//                            execute = 0;
//                            int index = 0;
//                            for (ScanCodeBean bean : data) {
//                                ask.setScanCode(bean.getWono());
//                                getPlanNoMessage(ask, index);
//                                index++;
//                            }
//                        } else {
//                            mView.onGetPlanNoScanList(listBaseBean);
//                            mView.hideLoading();
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        analysisThrowable(e);
//                        mView.hideLoading();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
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
