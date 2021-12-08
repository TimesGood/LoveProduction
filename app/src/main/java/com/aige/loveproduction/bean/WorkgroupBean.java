package com.aige.loveproduction.bean;

public class WorkgroupBean {
    private String id;
    private String wgCode;
    private String wgName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWgCode() {
        return wgCode;
    }

    public void setWgCode(String wgCode) {
        this.wgCode = wgCode;
    }

    public String getWgName() {
        return wgName;
    }

    public void setWgName(String wgName) {
        this.wgName = wgName;
    }

    @Override
    public String toString() {
        return "Workgroup{" +
                "id='" + id + '\'' +
                ", wgCode='" + wgCode + '\'' +
                ", wgName='" + wgName +
                '}';
    }
}
