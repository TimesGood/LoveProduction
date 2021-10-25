package com.aige.loveproduction.bean;

public class MessageBean {
    private String message;
    private String wono;
    private int status = 200;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWono() {
        return wono;
    }

    public void setWono(String wono) {
        this.wono = wono;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", wono='" + wono + '\'' +
                ", status=" + status +
                '}';
    }
}
