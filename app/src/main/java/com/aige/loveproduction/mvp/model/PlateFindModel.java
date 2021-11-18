package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.mvp.contract.PlateFindContract;
import com.aige.loveproduction.base.IBaseModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PlateFindModel implements PlateFindContract.Model{
    private final Gson gson = new Gson();
    public static IBaseModel newInstance() {
        return new PlateFindModel();
    }
    @Override
    public Observable<BaseBean<List<PlateBean>>> getPlateListByPackageCode(String barcode) {
        Map<String,String> map = new HashMap<>();
        map.put("barcode",barcode);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getPlateListByPackageCode(body);
    }
}
