package com.aige.loveproduction.contract;

import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.HandlerBean;
import com.aige.loveproduction.bean.MachineBean;
import com.aige.loveproduction.bean.WorkgroupBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface FactorySettingContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<MachineBean>>> getMachine();
        Observable<BaseBean<List<WorkgroupBean>>> getWorkgroupByMachineId(String machineId);
        Observable<BaseBean<List<HandlerBean>>> getHandlerByWorkgroupId(String workgroupId);
    }
    interface View extends IBaseView{
        //根据请求的方法指定加载那个请求的loading
        void showLoading(String method);
        void hideLoading(String method);
        //void onError(String method,String message);

        void onGetMachineSuccess(BaseBean<List<MachineBean>> bean,boolean isInit);
        void onGetWorkgroupSuccess(BaseBean<List<WorkgroupBean>> bean,boolean isInit);
        void onGetHandlerSuccess(BaseBean<List<HandlerBean>> bean,boolean isInit);
    }
    interface Presenter extends IBasePresenter<FactorySettingContract.View> {
        void getMachine(boolean isInit);
        void getWorkgroupByMachineId(String machineId,boolean isInit);
        void getHandlerByWorkgroupId(String workgroupId,boolean isInit);
    }
}
