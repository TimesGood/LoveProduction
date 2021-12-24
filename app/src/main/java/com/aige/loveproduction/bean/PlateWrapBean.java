package com.aige.loveproduction.bean;

import java.util.List;

public class PlateWrapBean {
    private String nickName;
    private List<PlateBean> list;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<PlateBean> getPlateBeans() {
        return list;
    }

    public void setPlateBeans(List<PlateBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PlateWrapBean{" +
                "nickName='" + nickName + '\'' +
                ", plateBeans=" + list +
                '}';
    }
}
