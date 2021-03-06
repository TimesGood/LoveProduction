package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface TransferContract {
    interface Model extends IBaseModel{
        Observable<BaseBean<List<TransferBean>>> getWonoByPackageCode(String packageCode, String operationEntity_Id,WonoAsk ask);
        Observable<BaseBean<ScanCodeBean>> getMessageByWono(WonoAsk ask);
    }
    interface View extends IBaseView{
        void onGetWonoByPackageCodeSuccess(BaseBean<List<TransferBean>> bean);
        void onGetMessageByWonoSuccess(List<ScanCodeBean> bean);

    }
    interface Presenter extends IBasePresenter<View> {
        void getWonoByPackageCode(String packageCode,String operationEntity_Id,WonoAsk ask);
        void getMessageByWono(List<TransferBean> beans,WonoAsk ask);

    }
}
