package com.aige.loveproduction.mvp.presenter;



import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.WorkScanContract;
import com.aige.loveproduction.mvp.model.WorkScanModel;
import com.aige.loveproduction.net.RxScheduler;
import com.aige.loveproduction.base.BasePresenter;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class WorkScanPresenter extends BasePresenter<WorkScanContract.View,WorkScanContract.Model> implements WorkScanContract.Presenter {

    @Override
    public WorkScanContract.Model bindModel() {
        return (WorkScanContract.Model) WorkScanModel.newInstance();
    }

    //获取扫描结果
    @Override
    public void getMessageByWono(WonoAsk ask) {
        checkViewAttached();
        mModel.getMessageByWono(ask).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<PlanNoMessageBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<PlanNoMessageBean> bean) {
                        mView.onGetMessageByWonoSuccess(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        analysisThrowable(e);
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

}
