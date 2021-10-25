package com.aige.loveproduction.model;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.contract.PlanNoScanContract;
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

public class PlanNoScanModel implements PlanNoScanContract.Model {
    private final Gson gson = new Gson();
    public static IBaseModel newInstance() {
        return new PlanNoScanModel();
    }
    @Override
    public Observable<BaseBean<List<TransferBean>>> getWonoByBatchNo(String batchNo, String opId, WonoAsk ask) {
        Map<String,String> map = new HashMap<>();
        map.put("batchNo",batchNo);
        map.put("operationEntity_Id",opId);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getWonoByBatchNo(body);
    }

    @Override
    public Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(WonoAsk ask) {
        return getApi().getMessageByWono(ask);
    }
}
