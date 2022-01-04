package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.mvp.contract.WorkScanContract;
import com.aige.loveproduction.base.IBaseModel;

import io.reactivex.rxjava3.core.Observable;

public class WorkScanModel implements WorkScanContract.Model {

    @Override
    public Observable<BaseBean<ScanCodeBean>> getMessageByWono(WonoAsk ask) {
        return getApi().getMessageByWono(ask);
    }
}
