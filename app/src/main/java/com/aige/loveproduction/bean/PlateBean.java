package com.aige.loveproduction.bean;

public class PlateBean {
    private String opName;
    private String modifyDate;
    private String operator;
    private String handler;
    private String detailName;
    private String matname;
    private String matProducer;
    private float fleng;
    private float fwidth;
    private int thk;
    private String info1;
    private int status = 200;



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getMatname() {
        return matname;
    }

    public void setMatname(String matname) {
        this.matname = matname;
    }

    public String getMatProducer() {
        return matProducer;
    }

    public void setMatProducer(String matProducer) {
        this.matProducer = matProducer;
    }

    public float getFleng() {
        return fleng;
    }

    public void setFleng(float fleng) {
        this.fleng = fleng;
    }

    public float getFwidth() {
        return fwidth;
    }

    public void setFwidth(float fwidth) {
        this.fwidth = fwidth;
    }

    public int getThk() {
        return thk;
    }

    public void setThk(int thk) {
        this.thk = thk;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    @Override
    public String toString() {
        return "PlateBean{" +
                "opName='" + opName + '\'' +
                ", modifyDate='" + modifyDate + '\'' +
                ", operator='" + operator + '\'' +
                ", handler='" + handler + '\'' +
                ", detailName='" + detailName + '\'' +
                ", matname='" + matname + '\'' +
                ", matProducer='" + matProducer + '\'' +
                ", fleng=" + fleng +
                ", fwidth=" + fwidth +
                ", thk=" + thk +
                ", info1='" + info1 + '\'' +
                ", status=" + status +
                '}';
    }
}
