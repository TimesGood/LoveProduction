package com.aige.loveproduction.bean;

public class HandlerBean {
    private String id;
    private String employeeName;
    private String user_Id;
    private int status = 200;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    @Override
    public String toString() {
        return "Handler{" +
                "id='" + id + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", user_Id='" + user_Id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
