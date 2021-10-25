package com.aige.loveproduction.bean;

public class HistoryLog {
    private String logText;

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    @Override
    public String toString() {
        return "HistoryLog{" +
                "logText='" + logText + '\'' +
                '}';
    }
}
