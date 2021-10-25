package com.aige.loveproduction.contract;

import com.aige.loveproduction.bean.BaseBean;
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
        Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(WonoAsk ask);
    }
    interface View extends IBaseView {
        void onGetWonoByBatchNoSuccess(BaseBean<List<TransferBean>> bean);
        void onGetMessageByWonoSuccess(BaseBean<List<ScanCodeBean>> bean);
    }
    interface Presenter extends IBasePresenter<PlanNoScanContract.View> {
        void getWonoByBatchNo(String batchNo, String opId,WonoAsk ask);
        void getMessageByWono(WonoAsk ask);

    }
}
