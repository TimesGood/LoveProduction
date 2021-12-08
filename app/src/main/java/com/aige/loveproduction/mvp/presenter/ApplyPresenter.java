package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.DownloadBean;
import com.aige.loveproduction.mvp.contract.ApplyContract;
import com.aige.loveproduction.mvp.model.ApplyModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

public class ApplyPresenter extends BasePresenter<ApplyContract.View, ApplyContract.Model> implements ApplyContract.Presenter{
    @Override
    public ApplyContract.Model bindModel() {
        return (ApplyContract.Model) ApplyModel.newInstance();
    }

    @Override
    public void getMPRByBatchNo(String barcode) {
        checkViewAttached();
        mModel.getMPRByBatchNo(barcode).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<DownloadBean>>(){
                    @Override
                    public void onStart(Disposable d) {
                        setDisposable(d);
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<DownloadBean> response) {
                        mView.onGetMPRByBatchNoSuccess(response);
                        DownloadBean downloadBean = response.get(0);
                        getFile(downloadBean.getFileUrl());
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
    public void getFile(String url) {
        checkViewAttached();
        mModel.getFile(url).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        setDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody body) {
                        mView.onGetFileSuccess(body);
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
