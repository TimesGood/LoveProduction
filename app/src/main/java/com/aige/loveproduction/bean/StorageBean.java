/**
 * Copyright 2021 bejson.com
 */
package com.aige.loveproduction.bean;

/**
 * Auto-generated: 2021-09-10 17:35:5
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class StorageBean {

    private String type;
    private String orderId;
    private String solutionName;
    private String binCode;
    private int packageTotal;
    private int notIntoPackage;
    private int notOutPackage;
    private int outPackage;
    private int sendPackage;
    private String notIntoCode;
    private int inPackage;
    private int totalPackage;
    private int code;
    private String msg;
    private int notInPackage;
    private int notSendPackage;
    private int weiBaoNumber;

    public int getTotalPackage() {
        return totalPackage;
    }

    public void setTotalPackage(int totalPackage) {
        this.totalPackage = totalPackage;
    }

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

    public int getNotInPackage() {
        return notInPackage;
    }

    public void setNotInPackage(int notInPackage) {
        this.notInPackage = notInPackage;
    }

    public int getNotSendPackage() {
        return notSendPackage;
    }

    public void setNotSendPackage(int notSendPackage) {
        this.notSendPackage = notSendPackage;
    }

    public int getWeiBaoNumber() {
        return weiBaoNumber;
    }

    public void setWeiBaoNumber(int weiBaoNumber) {
        this.weiBaoNumber = weiBaoNumber;
    }

    public int getInPackage() {
        return inPackage;
    }

    public void setInPackage(int inPackage) {
        this.inPackage = inPackage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }
    public String getSolutionName() {
        return solutionName;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }
    public String getBinCode() {
        return binCode;
    }

    public void setPackageTotal(int packageTotal) {
        this.packageTotal = packageTotal;
    }
    public int getPackageTotal() {
        return packageTotal;
    }

    public void setNotIntoPackage(int notIntoPackage) {
        this.notIntoPackage = notIntoPackage;
    }

    public int getNotOutPackage() {
        return notOutPackage;
    }

    public void setNotOutPackage(int notOutPackage) {
        this.notOutPackage = notOutPackage;
    }

    public int getNotIntoPackage() {
        return notIntoPackage;
    }

    public void setOutPackage(int outPackage) {
        this.outPackage = outPackage;
    }
    public int getOutPackage() {
        return outPackage;
    }

    public void setSendPackage(int sendPackage) {
        this.sendPackage = sendPackage;
    }
    public int getSendPackage() {
        return sendPackage;
    }

    public void setNotIntoCode(String notIntoCode) {
        this.notIntoCode = notIntoCode;
    }
    public String getNotIntoCode() {
        return notIntoCode;
    }

    @Override
    public String toString() {
        return "StorageBean{" +
                "orderId='" + orderId + '\'' +
                ", solutionName='" + solutionName + '\'' +
                ", binCode='" + binCode + '\'' +
                ", packageTotal=" + packageTotal +
                ", notIntoPackage=" + notIntoPackage +
                ", outPackage=" + outPackage +
                ", sendPackage=" + sendPackage +
                ", notIntoCode='" + notIntoCode + '\'' +
                '}';
    }
}