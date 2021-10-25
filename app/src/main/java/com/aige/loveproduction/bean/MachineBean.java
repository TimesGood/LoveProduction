package com.aige.loveproduction.bean;

public class MachineBean {
    private String id;
    private String mdCode;
    private String mdName;
    private String operation_Id;
    private String typeName;
    private int status = 200;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOperation_Id() {
        return operation_Id;
    }

    public void setOperation_Id(String operation_Id) {
        this.operation_Id = operation_Id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMdCode() {
        return mdCode;
    }

    public void setMdCode(String mdCode) {
        this.mdCode = mdCode;
    }

    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id='" + id + '\'' +
                ", mdCode='" + mdCode + '\'' +
                ", mdName='" + mdName + '\'' +
                ", operation_Id='" + operation_Id + '\'' +
                ", typeName='" + typeName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
