package com.aige.loveproduction.model;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.contract.WorkScanContract;
import com.aige.loveproduction.net.APIService;
import com.aige.loveproduction.net.RetrofitClient;
import com.aige.loveproduction.base.IBaseModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class WorkScanModel implements WorkScanContract.Model {
    public WorkScanModel() {
    }
    public static IBaseModel newInstance() {
        return new WorkScanModel();
    }

    @Override
    public Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(WonoAsk ask) {
        return getApi().getMessageByWono(ask);
    }
}
