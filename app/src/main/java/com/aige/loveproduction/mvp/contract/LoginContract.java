package com.aige.loveproduction.mvp.contract;


import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.UserBean;

import io.reactivex.rxjava3.core.Observable;

public interface LoginContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<UserBean>> getUser(String username, String password);
    }
    interface View extends IBaseView {
        void onGetUserSuccess(BaseBean<UserBean> bean);
    }
    interface Presenter extends IBasePresenter<View> {
        void getUser(String username,String password);
    }
}
