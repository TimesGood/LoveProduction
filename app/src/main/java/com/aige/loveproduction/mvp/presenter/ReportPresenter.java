package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.DownloadBean;
import com.aige.loveproduction.bean.ReportBean;
import com.aige.loveproduction.mvp.contract.ApplyContract;
import com.aige.loveproduction.mvp.contract.ReportContract;
import com.aige.loveproduction.mvp.model.ApplyModel;
import com.aige.loveproduction.mvp.model.ReportModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

public class ReportPresenter extends BasePresenter<ReportContract.View, ReportModel> implements ReportContract.Presenter{
    @Override
    public ReportModel bindModel() {
        return new ReportModel();
    }

    @Override
    public void getReportRefundMonth(String userName) {
        checkViewAttached();
        mModel.getReportRefundMonth(userName).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<ReportBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<ReportBean> response) {
                        mView.onReportRefundMonthSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getReportDirectlyMonth(String userName) {
        checkViewAttached();
        mModel.getReportDirectlyMonth(userName).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<ReportBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<ReportBean> response) {
                        mView.onReportDirectlyMonthSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getReportChannelOne(String userName) {
        checkViewAttached();
        mModel.getReportChannelOne(userName).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<ReportBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<ReportBean> response) {
                        mView.onReportChannelOneSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getReportChannelTwo(String userName) {
        checkViewAttached();
        mModel.getReportChannelTwo(userName).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<ReportBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<ReportBean> response) {
                        mView.onReportChannelTwoSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getReportOrderMonth(String userName) {
        checkViewAttached();
        mModel.getReportOrderMonth(userName).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<ReportBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<ReportBean> response) {
                        mView.onReportOrderMonthSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void getReportOrderDay(String userName) {
        checkViewAttached();
        mModel.getReportOrderDay(userName).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<ReportBean>>() {
                    @Override
                    public void onStart(Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onSuccess(List<ReportBean> response) {
                        mView.onReportOrderDaySuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.onError(message);
                        mView.hideLoading();
                    }

                    @Override
                    public void onNormalEnd() {
                        mView.hideLoading();
                    }
                });
    }
}
