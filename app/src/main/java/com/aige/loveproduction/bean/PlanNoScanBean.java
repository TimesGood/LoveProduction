package com.aige.loveproduction.bean;

public class PlanNoScanBean {
    private String packageCode;
    private String statusName;
    private String solutionName;
    private String inDate;
    private String deliveryDate;
    private String order_Id;
    private String rowId;
    private String status;
    private String createDate;
    private int responseCode = 200;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    @Override
    public String toString() {
        return "PlanNoScanBean{" +
                "packageCode='" + packageCode + '\'' +
                ", statusName='" + statusName + '\'' +
                ", solutionName='" + solutionName + '\'' +
                ", inDate='" + inDate + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", order_Id='" + order_Id + '\'' +
                ", rowId='" + rowId + '\'' +
                ", status='" + status + '\'' +
                ", createDate='" + createDate + '\'' +
                ", responseCode=" + responseCode +
                '}';
    }
}
