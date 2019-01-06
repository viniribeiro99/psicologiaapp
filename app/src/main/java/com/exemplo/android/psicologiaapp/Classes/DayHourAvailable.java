package com.exemplo.android.psicologiaapp.Classes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayHourAvailable implements Serializable{
    String day;
    String time;
    boolean isDe;

    public DayHourAvailable(String day, int hour, int minute, boolean isDe) {
        this.day = day;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setHours(hour);
        this.time = dateFormat.format(date);
        date.setMinutes(minute);
        this.isDe = isDe;
    }
    public DayHourAvailable(String day, String time, boolean isDe) {
        this.day = day;
        this.time = time;
        this.isDe = isDe;
    }
    public DayHourAvailable(String day, String time, String isDe) {
        this.day = day;
        this.time = time;
        if(isDe.equals("false")) {
            this.isDe = false;
        } else {
            this.isDe = true;
        }
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(int hour, int minute) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minute);
        this.time = dateFormat.format(date);
    }

    public boolean isDe() {
        return isDe;
    }

    public void setIsDe(boolean isDe) {
        this.isDe = isDe;
    }
    public boolean getIsDe() {
        return isDe;
    }
}
