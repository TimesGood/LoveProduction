package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.SpeciaBarAsk;
import com.aige.loveproduction.mvp.contract.SpecialShapedContract;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SpecialShapedModel extends PlateFindModel implements SpecialShapedContract.Model {

    @Override
    public Observable<BaseBean<PlateBean>> getPlateByPackageCode(String barcode) {
        Map<String,String> map = new HashMap<>();
        map.put("barcode",barcode);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getPlateByPackageCode(body);
    }

    @Override
    public Observable<BaseBean> getSpecialBar(SpeciaBarAsk ask) {
        return getApi().getSpecialBar(ask);
    }
}
