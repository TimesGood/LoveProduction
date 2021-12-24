package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.SpecialShapedContract;
import com.aige.loveproduction.mvp.contract.TransfersContract;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TransfersModel implements TransfersContract.Model {

    @Override
    public Observable<BaseBean<TransportBean>> getTransportVerification(String packageCode) {
        return getApi().getTransportVerification(packageCode);
    }

    @Override
    public Observable<BaseBean> transportScan(String packageCode, String transportName) {
        Map<String,String> map = new HashMap<>();
        map.put("packageCode",packageCode);
        map.put("transportName",transportName);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().transportScan(body);
    }

    @Override
    public Observable<BaseBean> transportSubmit(String packageCode) {
        Map<String,String> map = new HashMap<>();
        map.put("packageCode",packageCode);
        RequestBody body = RequestBody.Companion.create(
                gson.toJson(map),
                MediaType.parse("application/json;charset=utf-8"));
        return getApi().transportSubmit(body);
    }
}
