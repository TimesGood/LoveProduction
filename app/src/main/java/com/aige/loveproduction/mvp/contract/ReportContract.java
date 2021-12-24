package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.DownloadBean;
import com.aige.loveproduction.bean.PlateWrapBean;
import com.aige.loveproduction.bean.ReportBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface ReportContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<ReportBean>>> getReportRefundMonth(String userName);
        //直营单值月统计
        Observable<BaseBean<List<ReportBean>>> getReportDirectlyMonth(String userName);
        //渠道一部单值月统计
        Observable<BaseBean<List<ReportBean>>> getReportChannelOne(String userName);
        //渠道二部单值月统计
        Observable<BaseBean<List<ReportBean>>> getReportChannelTwo(String userName);
        //月下单统计
        Observable<BaseBean<List<ReportBean>>> getReportOrderMonth(String userName);
        //日下单统计
        Observable<BaseBean<List<ReportBean>>> getReportOrderDay(String userName);
    }
    interface View extends IBaseView {
        default void onReportRefundMonthSuccess(List<ReportBean> beans){};
        default void onReportDirectlyMonthSuccess(List<ReportBean> beans){};
        default void onReportChannelOneSuccess(List<ReportBean> beans){};
        default void onReportChannelTwoSuccess(List<ReportBean> beans){};
        default void onReportOrderMonthSuccess(List<ReportBean> beans){};
        default void onReportOrderDaySuccess(List<ReportBean> beans){};
    }
    interface Presenter extends IBasePresenter<View> {
        void getReportRefundMonth(String userName);
        void getReportDirectlyMonth(String userName);
        void getReportChannelOne(String userName);
        void getReportChannelTwo(String userName);
        void getReportOrderMonth(String userName);
        void getReportOrderDay(String userName);
    }
}
