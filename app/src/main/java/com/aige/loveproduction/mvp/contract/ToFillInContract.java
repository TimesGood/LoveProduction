package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;

import io.reactivex.rxjava3.core.Observable;

public interface ToFillInContract {
    interface Model extends IBaseModel {

    }
    interface View extends IBaseView {

    }
    interface Presenter extends IBasePresenter<ToFillInContract.View> {

    }
}
