package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface PlanNoScanContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<TransferBean>>> getWonoByBatchNo(String batchNo, String opId,WonoAsk ask);
        Observable<BaseBean<ScanCodeBean>> getMessageByWono(WonoAsk ask);
    }
    interface View extends IBaseView {
//        void onGetWonoByBatchNoSuccess(BaseBean<List<TransferBean>> bean);
//        void onGetMessageByWonoSuccess(BaseBean<List<ScanCodeBean>> bean);
        void onGetMessageByWonoSuccess(List<ScanCodeBean> bean);
    }
    interface Presenter extends IBasePresenter<View> {
        void getWonoByBatchNo(String batchNo, String opId,WonoAsk ask);
//        void getMessageByWono(WonoAsk ask);
        void getMessageByWono(List<TransferBean> beans,WonoAsk ask);
    }
}
