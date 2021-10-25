package com.aige.loveproduction.bean;

public class AdapterList {
    private String title;
    private String workOrderNo;
    private String orderId;
    private String yesScan;
    private String planNO;
    private String noScan;
    private String count;
    private String area;
    private String massage;

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkOrderNo() {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getYesScan() {
        return yesScan;
    }

    public void setYesScan(String yesScan) {
        this.yesScan = yesScan;
    }

    public String getPlanNO() {
        return planNO;
    }

    public void setPlanNO(String planNO) {
        this.planNO = planNO;
    }

    public String getNoScan() {
        return noScan;
    }

    public void setNoScan(String noScan) {
        this.noScan = noScan;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "AdapterList{" +
                "title='" + title + '\'' +
                ", workOrderNo='" + workOrderNo + '\'' +
                ", orderId='" + orderId + '\'' +
                ", yesScan='" + yesScan + '\'' +
                ", planNO='" + planNO + '\'' +
                ", noScan='" + noScan + '\'' +
                ", count='" + count + '\'' +
                ", area='" + area + '\'' +
                ", massage='" + massage + '\'' +
                '}';
    }
}
