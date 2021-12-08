package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.mvp.contract.CreateTaskContract;
import com.google.gson.Gson;

import io.reactivex.rxjava3.core.Observable;

public class CreateTaskModel implements CreateTaskContract.Model{
    private final Gson gson = new Gson();
    public static IBaseModel newInstance() {
        return new CreateTaskModel();
    }

    @Override
    public Observable<BaseBean<PrintBean>> getEntityByBarcode(String barcode) {
        return getApi().getEntityByBarcode(barcode);
    }

    @Override
    public Observable<BaseBean> submitPrint(PrintAsk ask) {
        return getApi().submitPrint(ask);
    }
}
