package com.bensmartsystem.backend.model;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

public class Timer {
    @Setter
    @Getter
    private int hour;
    @Setter
    @Getter
    private int minute;
    @Setter
    @Getter
    private int second;

    //Initializes clock on current time
    public Timer() {
        Calendar currentTime = Calendar.getInstance();
        this.hour = currentTime.get(Calendar.HOUR_OF_DAY);
        this.minute = currentTime.get(Calendar.MINUTE);
        this.second = currentTime.get(Calendar.SECOND);
    }

    public void tick(){
        this.second++;
        if(this.second == 60){
            second = 0;
            minute++;
            if(this.minute == 60){
                minute = 0;
                this.hour = (this.hour + 1) % 24;
            }
        }
    } 
}
