package com.aige.loveproduction.bean;

public class ReportBean {
    private String projectName;
    private String monthValue;
    private float monthTotal;
    private int monthCount;
    private float avgNumber;
    private String dayValue;
    private int dayCount;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(String monthValue) {
        this.monthValue = monthValue;
    }

    public float getMonthTotal() {
        return monthTotal;
    }

    public void setMonthTotal(float monthTotal) {
        this.monthTotal = monthTotal;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }

    public float getAvgNumber() {
        return avgNumber;
    }

    public void setAvgNumber(float avgNumber) {
        this.avgNumber = avgNumber;
    }

    public String getDayValue() {
        return dayValue;
    }

    public void setDayValue(String dayValue) {
        this.dayValue = dayValue;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    @Override
    public String toString() {
        return "ReportBean{" +
                "projectName='" + projectName + '\'' +
                ", monthValue='" + monthValue + '\'' +
                ", monthTotal=" + monthTotal +
                ", monthCount=" + monthCount +
                ", avgNumber=" + avgNumber +
                ", dayValue='" + dayValue + '\'' +
                ", dayCount=" + dayCount +
                '}';
    }
}
