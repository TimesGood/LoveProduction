package com.aige.loveproduction.contract;

import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface TransferContract {
    interface Model extends IBaseModel{
        Observable<BaseBean<List<TransferBean>>> getWonoByPackageCode(String packageCode, String operationEntity_Id,WonoAsk ask);
        Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(WonoAsk ask);
    }
    interface View extends IBaseView{
        void onGetWonoByPackageCodeSuccess(BaseBean<List<TransferBean>> bean);
        void onGetMessageByWonoSuccess(BaseBean<List<ScanCodeBean>> bean);

    }
    interface Presenter extends IBasePresenter<TransferContract.View> {
        void getWonoByPackageCode(String packageCode,String operationEntity_Id,WonoAsk ask);
        void getMessageByWono(WonoAsk ask);

    }
}
