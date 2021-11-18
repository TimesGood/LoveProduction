package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.TransportBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface TransfersContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<TransportBean>> getTransportVerification(String packageCode);
        Observable<BaseBean> transportScan(String packageCode,String transportName);
        Observable<BaseBean> transportSubmit(String packageCode);
    }
    interface View extends IBaseView {
        void onGetTransport(TransportBean bean);
        void onScanSuccess(BaseBean bean);
        void onSubmitSuccess(BaseBean bean);

    }
    interface Presenter extends IBasePresenter<TransfersContract.View> {
        void getTransportVerification(String packageCode);
        void transportScan(String packageCode,String transportName);
        void transportSubmit(String packageCode);

    }
}
