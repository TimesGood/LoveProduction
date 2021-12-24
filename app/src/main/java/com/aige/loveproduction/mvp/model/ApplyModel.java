package com.aige.loveproduction.mvp.model;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.bean.DownloadBean;
import com.aige.loveproduction.mvp.contract.ApplyContract;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

public class ApplyModel implements ApplyContract.Model {

    @Override
    public Observable<BaseBean<List<DownloadBean>>> getMPRByBatchNo(String barcode) {
        return getApi().getMPRByBatchNo(barcode);
    }

    @Override
    public Observable<ResponseBody> getFile(String url) {
        return getApi().getFile(url);
    }
}
