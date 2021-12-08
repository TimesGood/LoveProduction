package com.aige.loveproduction.bean;

public class PlanNoMessageBean {
    private int code;
    private String msg;
    private String orderId;
    private String wono;
    private int totalCnt;
    private double totalArea;
    private String planNo;

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

    public double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(double totalArea) {
        this.totalArea = totalArea;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
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
