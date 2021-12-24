package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.bean.ToFillInAsk;
import com.aige.loveproduction.bean.ToFillInBean;

import io.reactivex.rxjava3.core.Observable;

public interface ToFillInContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<ToFillInBean>> getToFillInData(String barcode);
        Observable<BaseBean> submitData(ToFillInAsk ask);

    }
    interface View extends IBaseView {
        void onGetToFillInDataSuccess(ToFillInBean bean);
        void onSubmitDataSuccess();

    }
    interface Presenter extends IBasePresenter<View> {
        void getToFillInData(String barcode);
        void submitData(ToFillInAsk ask);
    }
}
