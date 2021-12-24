/**
 * Copyright 2021 bejson.com
 */
package com.aige.loveproduction.bean;

import java.util.List;

/**
 * Auto-generated: 2021-12-09 13:53:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ToFillInAsk {

    private String barCode;
    private String name;
    private String mat;
    private String color;
    private String fleng;
    private String fwidth;
    private String thk;
    private String area;
    private String unitPrice;
    private String total;
    private String category;
    private String reason;
    private String department;
    private String finder;
    private String operator;
    private List<String> imgBase64;
    private String detailName;

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMat(String mat) {
        this.mat = mat;
    }

    public String getMat() {
        return mat;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setFleng(String fleng) {
        this.fleng = fleng;
    }

    public String getFleng() {
        return fleng;
    }

    public void setFwidth(String fwidth) {
        this.fwidth = fwidth;
    }

    public String getFwidth() {
        return fwidth;
    }

    public void setThk(String thk) {
        this.thk = thk;
    }

    public String getThk() {
        return thk;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setFinder(String finder) {
        this.finder = finder;
    }

    public String getFinder() {
        return finder;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public List<String> getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(List<String> imgBase64) {
        this.imgBase64 = imgBase64;
    }

    @Override
    public String toString() {
        return "ToFillInAsk{" +
                "barCode='" + barCode + '\'' +
                ", name='" + name + '\'' +
                ", mat='" + mat + '\'' +
                ", color='" + color + '\'' +
                ", fleng='" + fleng + '\'' +
                ", fwidth='" + fwidth + '\'' +
                ", thk='" + thk + '\'' +
                ", area='" + area + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", total='" + total + '\'' +
                ", category='" + category + '\'' +
                ", reason='" + reason + '\'' +
                ", department='" + department + '\'' +
                ", finder='" + finder + '\'' +
                ", operator='" + operator + '\'' +
                ", ImgBase64=" + imgBase64 +
                ", detailName='" + detailName + '\'' +
                '}';
    }
}