package com.exemplo.android.psicologiaapp.Classes;

public class Message {
    String text;
    String remetentId;
    String time;
    Integer millis;



    public Message(String text, String remetentId, String time, Integer millis) {
        this.text = text;
        this.remetentId = remetentId;
        this.time = time;
        this.millis = millis;

    }
    public Message(String text, String remetentId, String time) {
        this.text = text;
        this.remetentId = remetentId;
        this.time = time;

    }

    public Message() {
    }



    public Integer getMillis() {
        return millis;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRemetentId() {
        return remetentId;
    }

    public void setRemetentId(String remetentId) {
        this.remetentId = remetentId;
    }
}
