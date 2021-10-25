package com.aige.loveproduction.contract;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface PlateFindContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<PlateBean>>> getPlateListByPackageCode(String barcode);
    }
    interface View extends IBaseView {
        void onGetPlateListByPackageCodeSuccess(BaseBean<List<PlateBean>> bean);
    }
    interface Presenter extends IBasePresenter<PlateFindContract.View> {
        void getPlateListByPackageCode(String barcode);
    }
}
