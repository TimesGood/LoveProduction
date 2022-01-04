package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;

import io.reactivex.rxjava3.core.Observable;

public interface WorkScanContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<ScanCodeBean>> getMessageByWono(WonoAsk ask);
    }
    interface View extends IBaseView {
        void onScanMessageError(Throwable throwable,int index);
        //void onGetScanMessageSuccess(BaseBean<MessageBean> bean,int index);
        void onGetMessageByWonoSuccess(BaseBean<ScanCodeBean> bean);

    }
    interface Presenter extends IBasePresenter<View> {
        void getMessageByWono(WonoAsk ask);
    }
}
