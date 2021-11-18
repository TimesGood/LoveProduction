package com.aige.loveproduction.bean;

public class SpeciaBarAsk {
     private String barCode;
     private String name;
     private String mat;
     private String color;
     private String fleng;
     private String fwidth;
     private String thk;
     private String remark;
     private String step;
     private String operator;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMat() {
        return mat;
    }

    public void setMat(String mat) {
        this.mat = mat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFleng() {
        return fleng;
    }

    public void setFleng(String fleng) {
        this.fleng = fleng;
    }

    public String getFwidth() {
        return fwidth;
    }

    public void setFwidth(String fwidth) {
        this.fwidth = fwidth;
    }

    public String getThk() {
        return thk;
    }

    public void setThk(String thk) {
        this.thk = thk;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "SpeciaBarAsk{" +
                "barCode='" + barCode + '\'' +
                ", name='" + name + '\'' +
                ", mat='" + mat + '\'' +
                ", color='" + color + '\'' +
                ", fleng='" + fleng + '\'' +
                ", fwidth='" + fwidth + '\'' +
                ", thk='" + thk + '\'' +
                ", remark='" + remark + '\'' +
                ", step='" + step + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }
}
