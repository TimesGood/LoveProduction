package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.TransportBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface TransferVerifyContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<TransportBean>> getTransportVerification(String packageCode);
    }
    interface View extends IBaseView {
        void onGetTransport(TransportBean bean);
    }
    interface Presenter extends IBasePresenter<View> {
        void getTransportVerification(String packageCode);
    }
}
