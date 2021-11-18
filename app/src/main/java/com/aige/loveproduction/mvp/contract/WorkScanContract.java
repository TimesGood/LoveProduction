package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;

import io.reactivex.rxjava3.core.Observable;

public interface WorkScanContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(WonoAsk ask);
    }
    interface View extends IBaseView {
        void onScanMessageError(Throwable throwable,int index);
        //void onGetScanMessageSuccess(BaseBean<MessageBean> bean,int index);
        void onGetMessageByWonoSuccess(BaseBean<PlanNoMessageBean> bean);

    }
    interface Presenter extends IBasePresenter<WorkScanContract.View> {
        void getMessageByWono(WonoAsk ask);
    }
}
