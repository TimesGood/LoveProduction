package com.aige.loveproduction.model;

import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.contract.TransferContract;
import com.aige.loveproduction.net.RetrofitClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TransferModel implements TransferContract.Model {
    private final Gson gson = new Gson();
    public static IBaseModel newInatance() {
        return new TransferModel();
    }
    @Override
    public Observable<BaseBean<List<TransferBean>>> getWonoByPackageCode(String packageCode, String operationEntity_Id, WonoAsk ask) {
        Map<String,String> map = new HashMap<>();
        map.put("packageCode",packageCode);
        map.put("operationEntity_Id",operationEntity_Id);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getWonoByPackageCode(body);
    }

    @Override
    public Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(WonoAsk ask) {
        return getApi().getMessageByWono(ask);
    }
}
