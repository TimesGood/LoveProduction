/**
 * Copyright 2021 bejson.com
 */
package com.aige.loveproduction.bean;

import java.util.List;

/**
 * Auto-generated: 2021-12-09 13:57:25
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ToFillInBean {

    private BarEntity barEntity;
    private List<String> categoryListStr;
    private List<String> departmentListStr;
    private List<String> reasonListStr;

    public void setBarEntity(BarEntity barEntity) {
        this.barEntity = barEntity;
    }

    public BarEntity getBarEntity() {
        return barEntity;
    }

    public void setCategoryListStr(List<String> categoryList) {
        this.categoryListStr = categoryList;
    }

    public List<String> getCategoryListStr() {
        return categoryListStr;
    }

    public void setDepartmentListStr(List<String> departmentList) {
        this.departmentListStr = departmentList;
    }

    public List<String> getDepartmentListStr() {
        return departmentListStr;
    }

    public void setReasonListStr(List<String> reasonList) {
        this.reasonListStr = reasonList;
    }

    public List<String> getReasonListStr() {
        return reasonListStr;
    }

    @Override
    public String toString() {
        return "ToFillInBean{" +
                "barEntity=" + barEntity +
                ", categoryList=" + categoryListStr +
                ", departmentList=" + departmentListStr +
                ", reasonList=" + reasonListStr +
                '}';
    }

    public class BarEntity {

        private String barcode;
        private String detailName;
        private String matname;
        private String matProducer;
        private float fleng;
        private float fwidth;
        private int thk;
        private float area;
        private float total;
        private float unitPrice;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getBarcode() {
            return barcode;
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

        public void setArea(float area) {
            this.area = area;
        }

        public float getArea() {
            return area;
        }

        public void setTotal(float total) {
            this.total = total;
        }

        public float getTotal() {
            return total;
        }

        public void setUnitPrice(float unitPrice) {
            this.unitPrice = unitPrice;
        }

        public float getUnitPrice() {
            return unitPrice;
        }

        @Override
        public String toString() {
            return "BarEntity{" +
                    "barcode='" + barcode + '\'' +
                    ", detailName='" + detailName + '\'' +
                    ", matname='" + matname + '\'' +
                    ", matProducer='" + matProducer + '\'' +
                    ", fleng=" + fleng +
                    ", fwidth=" + fwidth +
                    ", thk=" + thk +
                    ", area=" + area +
                    ", total=" + total +
                    ", unitPrice=" + unitPrice +
                    '}';
        }
    }
}