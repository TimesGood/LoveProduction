package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface InStorageContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<BinFindBean>> getBinFind(String binCode);
        Observable<BaseBean<List<StorageBean>>> getScanPackage(String packageCode,String userName,String action,String binCode);
    }
    interface View extends IBaseView {
        void onGetScanPackageSuccess(BaseBean<List<StorageBean>> bean);
        void onGetBinFindSuccess(BaseBean<BinFindBean> bean);
    }
    interface Presenter extends IBasePresenter<InStorageContract.View> {
        void getScanPackage(String packageCode,String userName,String action,String binCode);
        void getBinFind(String binCode);
    }
}
