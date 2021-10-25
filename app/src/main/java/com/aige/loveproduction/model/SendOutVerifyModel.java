package com.aige.loveproduction.model;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.contract.SendOutVerifyContract;
import com.aige.loveproduction.net.APIService;
import com.aige.loveproduction.net.RetrofitClient;
import com.aige.loveproduction.base.IBaseModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SendOutVerifyModel implements SendOutVerifyContract.Model {
    private final Gson gson = new Gson();
    public static IBaseModel newInatance() {
        return new SendOutVerifyModel();
    }
    @Override
    public Observable<BaseBean<List<StorageBean>>> getSendOutVerify(String packageCode, String userName, String action, String binCode) {
        Map<String,String> map = new HashMap<>();
        map.put("packageCode",packageCode);
        map.put("userName",userName);
        map.put("action",action);
        map.put("binCode",binCode);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getSendGoodVerifi(body);
    }
}
