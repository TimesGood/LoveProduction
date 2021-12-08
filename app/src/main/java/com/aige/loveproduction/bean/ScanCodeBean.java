package com.aige.loveproduction.bean;

public class ScanCodeBean {
    private String wono;
    private String batchno;
    private String orderId;
    private String saoMiaoCount;
    private String planNo;
    private String weiSaoCount;
    private String totalCnt;
    private String totalArea;
    private String message;


    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSaoMiaoCount() {
        return saoMiaoCount;
    }

    public void setSaoMiaoCount(String saoMiaoCount) {
        this.saoMiaoCount = saoMiaoCount;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getWeiSaoCount() {
        return weiSaoCount;
    }

    public void setWeiSaoCount(String weiSaoCount) {
        this.weiSaoCount = weiSaoCount;
    }

    public String getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(String totalCnt) {
        this.totalCnt = totalCnt;
    }

    public String getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(String totalArea) {
        this.totalArea = totalArea;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWono() {
        return wono;
    }
    public void setWono(String wono) {
        this.wono = wono;
    }

    @Override
    public String toString() {
        return "ScanCodeBean{" +
                "wono='" + wono + '\'' +
                ", batchno='" + batchno + '\'' +
                ", orderId='" + orderId + '\'' +
                ", saoMiaoCount='" + saoMiaoCount + '\'' +
                ", planNo='" + planNo + '\'' +
                ", weiSaoCount='" + weiSaoCount + '\'' +
                ", totalCnt='" + totalCnt + '\'' +
                ", totalArea='" + totalArea + '\'' +
                ", message='" + message +
                '}';
    }
}
