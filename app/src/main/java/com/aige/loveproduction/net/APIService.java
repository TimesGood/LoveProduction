package com.aige.loveproduction.net;


import com.aige.loveproduction.base.BaseBean;
import com.aige.loveproduction.bean.BinFindBean;
import com.aige.loveproduction.bean.DownloadBean;
import com.aige.loveproduction.bean.HandlerBean;
import com.aige.loveproduction.bean.MachineBean;
import com.aige.loveproduction.bean.PlanNoMessageBean;
import com.aige.loveproduction.bean.PlateBean;
import com.aige.loveproduction.bean.PlateWrapBean;
import com.aige.loveproduction.bean.PrintAsk;
import com.aige.loveproduction.bean.PrintBean;
import com.aige.loveproduction.bean.ReportBean;
import com.aige.loveproduction.bean.ScanCodeBean;
import com.aige.loveproduction.bean.SpeciaBarAsk;
import com.aige.loveproduction.bean.StorageBean;
import com.aige.loveproduction.bean.ToFillInAsk;
import com.aige.loveproduction.bean.ToFillInBean;
import com.aige.loveproduction.bean.TransferBean;
import com.aige.loveproduction.bean.TransportBean;
import com.aige.loveproduction.bean.UserBean;
import com.aige.loveproduction.bean.WonoAsk;
import com.aige.loveproduction.bean.WorkgroupBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface APIService {

    //登录
    @POST("/api/UserLogin")
    Observable<BaseBean<UserBean>> login(@Body RequestBody body);

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
    @POST("/api/ScanQRcode/Scan_BOM_ItemDetailByBarcode_V3")
    Observable<BaseBean<PlateWrapBean>> getPlateListByPackageCode(@Body RequestBody body);

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
    @Headers({HostType.CONNECT_TIMEOUT+":30", HostType.READ_TIMEOUT+":30", HostType.WRITE_TIMEOUT+":30"})
    @POST("/api/ScanQRcode/Scan_WorkOrder")
    Observable<BaseBean<ScanCodeBean>> getMessageByWono(@Body WonoAsk body);

    //发货验证
    @POST("/api/ScanQRcode/Scan_SendGoodsVerification")
    Observable<BaseBean<List<StorageBean>>> getSendGoodVerifi(@Body RequestBody body);

    //根据包装码获取工单号
    @POST("/api/ScanQRcode/Scan_PackageCodeReturnWono")
    Observable<BaseBean<List<TransferBean>>> getWonoByPackageCode(@Body RequestBody body);

    //异形板件扫描
    @POST("/api/ScanQRcode/Scan_BOM_ItemDetailByBarcodeV2")
    Observable<BaseBean<PlateBean>> getPlateByPackageCode(@Body RequestBody body);

    //提交异形板件数据
    @POST("/api/ReckonSalary/SpecialBar/CreateSpecialBar")
    Observable<BaseBean> getSpecialBar(@Body SpeciaBarAsk ask);

    //混批扫描
    @POST("/api/ScanQRcode/Scan_ProcScanBatchNo_V2_HunPi")
    Observable<BaseBean<List<ScanCodeBean>>> getHunPiByBatchNo(@Body RequestBody body);

    //下载
    @Streaming
    @Headers("Connection:close")
    @GET()
    Observable<ResponseBody> getFile(@Url String url);

    //获取MPR文件
    @GET("/api/Paperless/Folder/GetMPRByBarCode")
    Observable<BaseBean<List<DownloadBean>>> getMPRByBatchNo(@Query("BarCode") String barcode);
    @GET("/api/Paperless/Folder/GetMPRByBarCodeV2Ip10")
    Observable<BaseBean<List<String>>> getMPRByBatchNoV2(@Query("BarCode") String barcode);

    //转运扫描获取列表
    @GET("/api/EBAP/Transport/TransportVerification")
    Observable<BaseBean<TransportBean>> getTransportVerification(@Query("packageCode") String packageCode);
    //转运扫描
    @POST("/api/EBAP/Transport/TransportScan")
    Observable<BaseBean> transportScan(@Body RequestBody body);
    //转运扫描提交数据
    @POST("/api/EBAP/Transport/ConfirmScan")
    Observable<BaseBean> transportSubmit(@Body RequestBody body);

    //获取打印信息
    @GET("/api/print/Print/GetEntityByBarcode")
    Observable<BaseBean<PrintBean>> getEntityByBarcode(@Query("BarCode") String barcode);
    @POST("/api/print/Print/CreatePrintInfo")
    Observable<BaseBean> submitPrint(@Body PrintAsk ask);

    //内改补数据采集
    @GET("/api/Examine/ic/InnerChange/QueryBOMItemDetailModelByBarcore")
    Observable<BaseBean<ToFillInBean>> getToFillInData(@Query("Barcode") String barcode);
    @Headers({HostType.BASE_URL+":"+ApiConstants.base_url_one})
    @POST("/api/ExamineOrder/InnerChange/Create")
    Observable<BaseBean> submitData(@Body ToFillInAsk ask);



    /*******************************************报表****************************************************************/
    //月回款统计
    @POST("/api/EBAP/Dashboard/QueryReportChannelMonthTotal")
    Observable<BaseBean<List<ReportBean>>> getReportRefundMonth(@Body RequestBody body);
    //直营单值月统计
    @POST("/api/EBAP/Dashboard/QueryReportZhiYingDanZhi")
    Observable<BaseBean<List<ReportBean>>> getReportDirectlyMonth(@Body RequestBody body);
    //渠道一部单值月统计
    @POST("/api/EBAP/Dashboard/QueryReportQuDaoOneDanZhi")
    Observable<BaseBean<List<ReportBean>>> getReportChannelOne(@Body RequestBody body);
    //渠道二部单值月统计
    @POST("/api/EBAP/Dashboard/QueryReportQuDaoTooDanZhi")
    Observable<BaseBean<List<ReportBean>>> getReportChannelTwo(@Body RequestBody body);
    //月下单统计
    @POST("/api/EBAP/Dashboard/QueryReportMonthOrder")
    Observable<BaseBean<List<ReportBean>>> getReportOrderMonth(@Body RequestBody body);
    //日下单统计
    @POST("/api/EBAP/Dashboard/QueryReportDayOrder")
    Observable<BaseBean<List<ReportBean>>> getReportOrderDay(@Body RequestBody body);



}
