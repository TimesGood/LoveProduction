package com.aige.loveproduction.bean;

public class MessageBean {
    private String message;
    private String wono;


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
                ", wono='" + wono +
                '}';
    }
}
