package com.aige.loveproduction.bean;

public class WonoAsk {
    private String scanCode;
    private String operationType;
    private String machineId;
    private String operationId;
    private String workGroupId;
    private String userName;
    private String employeeId;

    public String getScanCode() {
        return scanCode;
    }

    public void setScanCode(String scanCode) {
        this.scanCode = scanCode;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getWorkGroupId() {
        return workGroupId;
    }

    public void setWorkGroupId(String workGroupId) {
        this.workGroupId = workGroupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "WonoAsk{" +
                "scanCode='" + scanCode + '\'' +
                ", operationType='" + operationType + '\'' +
                ", machineId=" + machineId +
                ", operationId=" + operationId +
                ", workGroupId=" + workGroupId +
                ", userName='" + userName + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
