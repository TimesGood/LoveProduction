package com.aige.loveproduction.mvp.presenter;

import com.aige.loveproduction.base.BasePresenter;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.mvp.contract.CreateTaskContract;
import com.aige.loveproduction.mvp.contract.ToFillInContract;
import com.aige.loveproduction.mvp.model.CreateTaskModel;
import com.aige.loveproduction.mvp.model.ToFillInModel;
import com.aige.loveproduction.net.BaseObserver;
import com.aige.loveproduction.net.RxScheduler;

import io.reactivex.rxjava3.disposables.Disposable;

public class ToFillInPresenter extends BasePresenter<ToFillInContract.View,ToFillInContract.Model> implements ToFillInContract.Presenter{
    @Override
    public ToFillInContract.Model bindModel() {
        return (ToFillInContract.Model) ToFillInModel.newInstance();
    }

}
