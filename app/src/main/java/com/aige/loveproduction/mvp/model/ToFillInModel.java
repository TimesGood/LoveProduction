package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.ToFillInAsk;
import com.aige.loveproduction.bean.ToFillInBean;
import com.aige.loveproduction.mvp.contract.ToFillInContract;
import com.aige.loveproduction.net.RetrofitClient;
import com.google.gson.Gson;

import io.reactivex.rxjava3.core.Observable;

public class ToFillInModel implements ToFillInContract.Model{

    @Override
    public Observable<BaseBean<ToFillInBean>> getToFillInData(String barcode) {
        return getApi().getToFillInData(barcode);
    }

    @Override
    public Observable<BaseBean> submitData(ToFillInAsk ask) {
        return getApi().submitData(ask);
    }
}
