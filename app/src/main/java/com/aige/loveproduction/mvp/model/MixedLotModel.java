package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.MixedLotContract;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MixedLotModel implements MixedLotContract.Model {

    @Override
    public Observable<BaseBean<List<ScanCodeBean>>> getHunPiByBatchNo(String batchNo, String opId, String solutionName, WonoAsk ask) {
        Map<String,String> map = new HashMap<>();
        map.put("batchNo",batchNo);
        map.put("operationEntity_Id",opId);
        map.put("solutionName",solutionName);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getHunPiByBatchNo(body);
    }

    @Override
    public Observable<BaseBean<ScanCodeBean>> getMessageByWono(WonoAsk ask) {
        return getApi().getMessageByWono(ask);
    }
}
