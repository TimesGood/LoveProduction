package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.mvp.contract.TransferVerifyContract;
import com.aige.loveproduction.mvp.contract.TransfersContract;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class TransferVerifyModel implements TransferVerifyContract.Model {

    @Override
    public Observable<BaseBean<TransportBean>> getTransportVerification(String packageCode) {
        return getApi().getTransportVerification(packageCode);
    }
}
