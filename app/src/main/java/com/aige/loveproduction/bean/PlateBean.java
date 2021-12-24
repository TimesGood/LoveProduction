package com.aige.loveproduction.bean;

import java.util.Date;

public class PlateBean {

//    private String opName;
//    private String modifyDate;
//    private String operator;
//    private String handler;
//    private String detailName;
//    private String matname;
//    private String matProducer;
//    private float fleng;
//    private float fwidth;
//    private int thk;
//    private String info1;
//
//
//    public String getOpName() {
//        return opName;
//    }
//
//    public void setOpName(String opName) {
//        this.opName = opName;
//    }
//
//    public String getModifyDate() {
//        return modifyDate;
//    }
//
//    public void setModifyDate(String modifyDate) {
//        this.modifyDate = modifyDate;
//    }
//
//    public String getOperator() {
//        return operator;
//    }
//
//    public void setOperator(String operator) {
//        this.operator = operator;
//    }
//
//    public String getHandler() {
//        return handler;
//    }
//
//    public void setHandler(String handler) {
//        this.handler = handler;
//    }
//
//    public String getDetailName() {
//        return detailName;
//    }
//
//    public void setDetailName(String detailName) {
//        this.detailName = detailName;
//    }
//
//    public String getMatname() {
//        return matname;
//    }
//
//    public void setMatname(String matname) {
//        this.matname = matname;
//    }
//
//    public String getMatProducer() {
//        return matProducer;
//    }
//
//    public void setMatProducer(String matProducer) {
//        this.matProducer = matProducer;
//    }
//
//    public float getFleng() {
//        return fleng;
//    }
//
//    public void setFleng(float fleng) {
//        this.fleng = fleng;
//    }
//
//    public float getFwidth() {
//        return fwidth;
//    }
//
//    public void setFwidth(float fwidth) {
//        this.fwidth = fwidth;
//    }
//
//    public int getThk() {
//        return thk;
//    }
//
//    public void setThk(int thk) {
//        this.thk = thk;
//    }
//
//    public String getInfo1() {
//        return info1;
//    }
//
//    public void setInfo1(String info1) {
//        this.info1 = info1;
//    }
//
//    @Override
//    public String toString() {
//        return "PlateBean{" +
//                "opName='" + opName + '\'' +
//                ", modifyDate='" + modifyDate + '\'' +
//                ", operator='" + operator + '\'' +
//                ", handler='" + handler + '\'' +
//                ", detailName='" + detailName + '\'' +
//                ", matname='" + matname + '\'' +
//                ", matProducer='" + matProducer + '\'' +
//                ", fleng=" + fleng +
//                ", fwidth=" + fwidth +
//                ", thk=" + thk +
//                ", info1='" + info1 +
//                '}';
//    }
private String solutionConfigName;
    private String opName;
    private String modifyDate;
    private String operator;
    private String handler;
    private String planNo;
    private String apS_Code;
    private String planStartDate;
    private String planEndDate;
    private String supplier;
    private String detailName;
    private String matname;
    private String matProducer;
    private float fleng;
    private float fwidth;
    private int thk;
    private String info1;

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public void setSolutionConfigName(String solutionConfigName) {
        this.solutionConfigName = solutionConfigName;
    }
    public String getSolutionConfigName() {
        return solutionConfigName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }
    public String getOpName() {
        return opName;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }
    public String getModifyDate() {
        return modifyDate;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getOperator() {
        return operator;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
    public String getHandler() {
        return handler;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }
    public String getPlanNo() {
        return planNo;
    }

    public void setApS_Code(String apS_Code) {
        this.apS_Code = apS_Code;
    }
    public String getApS_Code() {
        return apS_Code;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }
    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }
    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getSupplier() {
        return supplier;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }
    public String getDetailName() {
        return detailName;
    }

    public void setMatname(String matname) {
        this.matname = matname;
    }
    public String getMatname() {
        return matname;
    }

    public void setMatProducer(String matProducer) {
        this.matProducer = matProducer;
    }
    public String getMatProducer() {
        return matProducer;
    }

    public void setFleng(float fleng) {
        this.fleng = fleng;
    }
    public float getFleng() {
        return fleng;
    }

    public void setFwidth(float fwidth) {
        this.fwidth = fwidth;
    }
    public float getFwidth() {
        return fwidth;
    }

    public void setThk(int thk) {
        this.thk = thk;
    }
    public int getThk() {
        return thk;
    }

    @Override
    public String toString() {
        return "PlateBean{" +
                "solutionConfigName='" + solutionConfigName + '\'' +
                ", opName='" + opName + '\'' +
                ", modifyDate=" + modifyDate +
                ", operator='" + operator + '\'' +
                ", handler='" + handler + '\'' +
                ", planNo='" + planNo + '\'' +
                ", apS_Code='" + apS_Code + '\'' +
                ", planStartDate=" + planStartDate +
                ", planEndDate=" + planEndDate +
                ", supplier='" + supplier + '\'' +
                ", detailName='" + detailName + '\'' +
                ", matname='" + matname + '\'' +
                ", matProducer='" + matProducer + '\'' +
                ", fleng=" + fleng +
                ", fwidth=" + fwidth +
                ", thk=" + thk +
                '}';
    }
}
