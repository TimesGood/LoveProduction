package com.aige.loveproduction.mvp.contract;

import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.base.IBaseModel;
import com.aige.loveproduction.base.IBasePresenter;
import com.aige.loveproduction.base.IBaseView;
import com.aige.loveproduction.bean.DownloadBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

public interface ApplyContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<DownloadBean>>> getMPRByBatchNo(String barcode);
        Observable<ResponseBody> getFile(String url);

    }
    interface View extends IBaseView {
        void onGetMPRByBatchNoSuccess(List<DownloadBean> beans);
        void onGetFileSuccess(ResponseBody body);
    }
    interface Presenter extends IBasePresenter<View> {
        void getMPRByBatchNo(String batchNo);
        void getFile(String url);
    }
}
