package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.PlateWrapBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface PlateFindContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<PlateWrapBean>> getPlateListByPackageCode(String barcode);
    }
    interface View extends IBaseView {
        void onGetPlateListByPackageCodeSuccess(PlateWrapBean bean);
    }
    interface Presenter extends IBasePresenter<View> {
        void getPlateListByPackageCode(String barcode);
    }
}
