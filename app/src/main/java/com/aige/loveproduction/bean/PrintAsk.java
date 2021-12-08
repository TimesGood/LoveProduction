/**
 * Copyright 2021 bejson.com
 */
package com.aige.loveproduction.bean;

/**
 * Auto-generated: 2021-12-02 16:4:22
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PrintAsk {

    private String barCode;
    private String codeType;
    private int num;
    private String printType;
    private float area;
    private String creator;
    private String step;
    private int paper;

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getPrintType() {
        return printType;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getArea() {
        return area;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStep() {
        return step;
    }

    public void setPaper(int paper) {
        this.paper = paper;
    }

    public int getPaper() {
        return paper;
    }

    @Override
    public String toString() {
        return "PrintAsk{" +
                "barCode='" + barCode + '\'' +
                ", codeType='" + codeType + '\'' +
                ", num=" + num +
                ", printType='" + printType + '\'' +
                ", area=" + area +
                ", creator='" + creator + '\'' +
                ", step='" + step + '\'' +
                ", paper=" + paper +
                '}';
    }
}