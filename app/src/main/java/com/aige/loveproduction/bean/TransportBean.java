/**
  * Copyright 2021 bejson.com 
  */
package com.aige.loveproduction.bean;

import java.util.List;


public class TransportBean {
    private List<TransportBeans> list;
    private int weiBaoNumber;
    private int notTransport;

    public List<TransportBeans> getList() {
        return list;
    }

    public void setList(List<TransportBeans> list) {
        this.list = list;
    }

    public int getWeiBaoNumber() {
        return weiBaoNumber;
    }

    public void setWeiBaoNumber(int weiBaoNumber) {
        this.weiBaoNumber = weiBaoNumber;
    }

    public int getNotTransport() {
        return notTransport;
    }

    public void setNotTransport(int notTransport) {
        this.notTransport = notTransport;
    }

    @Override
    public String toString() {
        return "TransportBean{" +
                "list=" + list +
                ", weiBaoNumber='" + weiBaoNumber + '\'' +
                ", notTransport='" + notTransport + '\'' +
                '}';
    }

    public static class TransportBeans {
        private String packageCode;
        private String type;
        private String transportDate;
        private String confirmDate;

        public String getPackageCode() {
            return packageCode;
        }

        public void setPackageCode(String packageCode) {
            this.packageCode = packageCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTransportDate() {
            return transportDate;
        }

        public void setTransportDate(String transportDate) {
            this.transportDate = transportDate;
        }

        public String getConfirmDate() {
            return confirmDate;
        }

        public void setConfirmDate(String confirmDate) {
            this.confirmDate = confirmDate;
        }

        @Override
        public String toString() {
            return "TransportBeans{" +
                    "packageCode='" + packageCode + '\'' +
                    ", type='" + type + '\'' +
                    ", transportDate='" + transportDate + '\'' +
                    ", confirmDate='" + confirmDate + '\'' +
                    '}';
        }
    }
}