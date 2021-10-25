package com.aige.loveproduction.net;


import com.aige.loveproduction.bean.BaseBean;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.HandlerBean;
import com.aige.loveproduction.bean.MachineBean;
import com.aige.loveproduction.bean.MessageBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.PlanNoScanBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.UserBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.bean.WorkgroupBean;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIService {

    //登录
    @POST("/api/UserLogin")
    Observable<BaseBean<UserBean>> getUser(@Body RequestBody body);

    //获取所有机器列表
    @POST("/api/FactorySettings/GetMachineList")
    Observable<BaseBean<List<MachineBean>>> getMachine();

    //根据机器Id获取工作组
    @POST("/api/FactorySettings/GetWorkGroupByMachineId")
    Observable<BaseBean<List<WorkgroupBean>>> getWorkgroupByMachineId(@Body RequestBody body);

    //根据工作组获取操作人列表
    @POST("/api/FactorySettings/GetWorkGroupDetailList")
    Observable<BaseBean<List<HandlerBean>>> getHandlerByWorkgroupId(@Body RequestBody body);

    //获取板材列表
    @POST("/api/ScanQRcode/Scan_BOM_ItemDetailByBarcode")
    Observable<BaseBean<List<PlateBean>>> getPlateListByPackageCode(@Body RequestBody body);

    //库位查询
    @POST("/api/ScanQRcode/Scan_LocationQuery")
    Observable<BaseBean<BinFindBean>> getBinFind(@Body RequestBody body);

    //入库、出库、发货
    @POST("/api/ScanQRcode/Scan_PackageCode")
    Observable<BaseBean<List<StorageBean>>> getScanPackageCode(@Body RequestBody body);

    //根据批次获取工单号
    @POST("/api/ScanQRcode/Scan_ProcScanBatchNo")
    Observable<BaseBean<List<TransferBean>>> getWonoByBatchNo(@Body RequestBody body);

    //工单扫描获取扫描结果
    @Headers({"CONNECT_TIMEOUT:30", "READ_TIMEOUT:30", "WRITE_TIMEOUT:30"})
    @POST("/api/ScanQRcode/Scan_WorkOrder")
    Observable<BaseBean<PlanNoMessageBean>> getMessageByWono(@Body WonoAsk body);

    //发货验证
    @POST("/api/ScanQRcode/Scan_SendGoodsVerification")
    Observable<BaseBean<List<StorageBean>>> getSendGoodVerifi(@Body RequestBody body);

    //根据包装码获取工单号
    @POST("/api/ScanQRcode/Scan_PackageCodeReturnWono")
    Observable<BaseBean<List<TransferBean>>> getWonoByPackageCode(@Body RequestBody body);
}
