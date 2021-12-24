package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.SpeciaBarAsk;
import com.aige.loveproduction.mvp.model.PlateFindModel;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface SpecialShapedContract {
    interface Model extends IBaseModel{
        Observable<BaseBean<PlateBean>> getPlateByPackageCode(String barcode);
        Observable<BaseBean> getSpecialBar(SpeciaBarAsk ask);
    }
    interface View extends IBaseView {
        void onGetPlateListByPackageCodeSuccess(PlateBean bean);
        void submitShowLoading();
        void submitHideLoading();
        void onGetSpecialBar(BaseBean bean);
    }
    interface Presenter extends IBasePresenter<View> {
        void getSpecialBar(SpeciaBarAsk ask);
    }
}
