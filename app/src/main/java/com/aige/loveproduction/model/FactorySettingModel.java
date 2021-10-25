package com.aige.loveproduction.model;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.HandlerBean;
import com.aige.loveproduction.bean.MachineBean;
import com.aige.loveproduction.bean.WorkgroupBean;
import com.aige.loveproduction.contract.FactorySettingContract;
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

public class FactorySettingModel implements FactorySettingContract.Model {
    private final Gson gson = new Gson();

    /**
     * 实例化Model
     * @return Model对象
     */
    public static IBaseModel newInstance() {
        return new FactorySettingModel();
    }


    @Override
    public Observable<BaseBean<List<MachineBean>>> getMachine() {
        return getApi().getMachine();
    }

    @Override
    public Observable<BaseBean<List<WorkgroupBean>>> getWorkgroupByMachineId(String machineId) {
        Map<String,String> map = new HashMap<>();
        map.put("machineId",machineId);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getWorkgroupByMachineId(body);
    }

    @Override
    public Observable<BaseBean<List<HandlerBean>>> getHandlerByWorkgroupId(String workgroupId) {
        Map<String,String> map = new HashMap<>();
        map.put("workGroup_Id",workgroupId);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getHandlerByWorkgroupId(body);
    }
}
