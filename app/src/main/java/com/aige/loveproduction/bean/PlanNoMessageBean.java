package com.aige.loveproduction.bean;

public class PlanNoMessageBean {
    private int code;
    private String msg;
    private String orderId;
    private String wono;
    private int totalCnt;
    private float totalArea;
    private String planNo;
    private String batchno;
    private String saoMiaoCount;
    private String weiSaoCount;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getWono() {
        return wono;
    }

    public void setWono(String wono) {
        this.wono = wono;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public float getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(float totalArea) {
        this.totalArea = totalArea;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getSaoMiaoCount() {
        return saoMiaoCount;
    }

    public void setSaoMiaoCount(String saoMiaoCount) {
        this.saoMiaoCount = saoMiaoCount;
    }

    public String getWeiSaoCount() {
        return weiSaoCount;
    }

    public void setWeiSaoCount(String weiSaoCount) {
        this.weiSaoCount = weiSaoCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PlanNoMessageBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", orderId='" + orderId + '\'' +
                ", wono='" + wono + '\'' +
                ", totalCnt=" + totalCnt +
                ", totalArea=" + totalArea +
                ", planNo='" + planNo +
                '}';
    }
}
