package com.aige.loveproduction.mvp.model;


import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;

import com.aige.loveproduction.bean.ReportBean;
import com.aige.loveproduction.mvp.contract.ReportContract;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ReportModel implements ReportContract.Model {

    private RequestBody getBody(String para) {
        Map<String,String> map = new HashMap<>();
        map.put("userName",para);
        return RequestBody.Companion.create(
                gson.toJson(map),
                MediaType.parse("application/json;charset=utf-8"));
    }

    @Override
    public Observable<BaseBean<List<ReportBean>>> getReportRefundMonth(String userName) {
        return getApi().getReportRefundMonth(getBody(userName));
    }

    @Override
    public Observable<BaseBean<List<ReportBean>>> getReportDirectlyMonth(String userName) {
        return getApi().getReportDirectlyMonth(getBody(userName));
    }

    @Override
    public Observable<BaseBean<List<ReportBean>>> getReportChannelOne(String userName) {
        return getApi().getReportChannelOne(getBody(userName));
    }

    @Override
    public Observable<BaseBean<List<ReportBean>>> getReportChannelTwo(String userName) {
        return getApi().getReportChannelTwo(getBody(userName));
    }

    @Override
    public Observable<BaseBean<List<ReportBean>>> getReportOrderMonth(String userName) {
        return getApi().getReportOrderMonth(getBody(userName));
    }

    @Override
    public Observable<BaseBean<List<ReportBean>>> getReportOrderDay(String userName) {
        return getApi().getReportOrderDay(getBody(userName));
    }
}
