package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.bean.StorageBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface CreateTaskContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<PrintBean>> getEntityByBarcode(String barcode);
        Observable<BaseBean> submitPrint(PrintAsk ask);
    }
    interface View extends IBaseView {
        void onGetEntityByBarcodeSuccess(PrintBean bean);
        void onSubmitPrintSuccess();
    }
    interface Presenter extends IBasePresenter<CreateTaskContract.View> {
        void getEntityByBarcode(String barcode);
        void submitPrint(PrintAsk ask);
    }
}
