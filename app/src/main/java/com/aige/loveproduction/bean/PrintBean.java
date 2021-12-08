/**
  * Copyright 2021 bejson.com 
  */
package com.aige.loveproduction.bean;

/**
 * Auto-generated: 2021-12-02 16:6:4
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PrintBean {

    private int thk;
    private String orderId;
    private String salesOrderId;
    private int fwidth;
    private int fleng;
    private float assemblyArea;
    private String barcode;
    private String codeType;
    public void setThk(int thk) {
         this.thk = thk;
     }
     public int getThk() {
         return thk;
     }

    public void setOrderId(String orderId) {
         this.orderId = orderId;
     }
     public String getOrderId() {
         return orderId;
     }

    public void setSalesOrderId(String salesOrderId) {
         this.salesOrderId = salesOrderId;
     }
     public String getSalesOrderId() {
         return salesOrderId;
     }

    public void setFwidth(int fwidth) {
         this.fwidth = fwidth;
     }
     public int getFwidth() {
         return fwidth;
     }

    public void setFleng(int fleng) {
         this.fleng = fleng;
     }
     public int getFleng() {
         return fleng;
     }

    public void setAssemblyArea(float assemblyArea) {
         this.assemblyArea = assemblyArea;
     }
     public float getAssemblyArea() {
         return assemblyArea;
     }

    public void setBarcode(String barcode) {
         this.barcode = barcode;
     }
     public String getBarcode() {
         return barcode;
     }

    public void setCodeType(String codeType) {
         this.codeType = codeType;
     }
     public String getCodeType() {
         return codeType;
     }

    @Override
    public String toString() {
        return "PrintBean{" +
                "thk=" + thk +
                ", orderId='" + orderId + '\'' +
                ", salesOrderId='" + salesOrderId + '\'' +
                ", fwidth=" + fwidth +
                ", fleng=" + fleng +
                ", assemblyArea=" + assemblyArea +
                ", barcode='" + barcode + '\'' +
                ", codeType='" + codeType + '\'' +
                '}';
    }
}