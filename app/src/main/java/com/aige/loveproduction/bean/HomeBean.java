package com.aige.loveproduction.bean;

public class HomeBean {
    private int id;
    private int img_id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "HomeBean{" +
                "id=" + id +
                ", img_id=" + img_id +
                ", text='" + text + '\'' +
                '}';
    }
}
