package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface MixedLotContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<ScanCodeBean>>> getHunPiByBatchNo(String batchN0, String opId, String solutionName,WonoAsk ask);
        Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(WonoAsk ask);
    }
    interface View extends IBaseView {
        void onGetHunPiByBatchNoSuccess(List<ScanCodeBean> bean);
        void onGetMessageByWonoSuccess(BaseBean<List<ScanCodeBean>> bean);
    }
    interface Presenter extends IBasePresenter<View> {
        void getHunPiByBatchNo(String batchNo,String opId,String solutionName,WonoAsk ask);
        void getMessageByWono(WonoAsk ask);
    }
}
