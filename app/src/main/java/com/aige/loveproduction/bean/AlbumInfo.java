package com.aige.loveproduction.bean;

/**
 * 专辑信息类
 */
public class AlbumInfo {

    /**
     * 封面
     */
    private String icon;
    /**
     * 名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 选中
     */
    private boolean select;

    public AlbumInfo(String icon, String name, String remark, boolean select) {
        this.icon = icon;
        this.name = name;
        this.remark = remark;
        this.select = select;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

    public boolean isSelect() {
        return select;
    }
}