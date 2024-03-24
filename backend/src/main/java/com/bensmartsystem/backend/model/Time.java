package com.bensmartsystem.backend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Calendar;

@Setter
@Getter
@Entity
public class Time
{
    private int hour;
    private int minute;
    private int second;

    //Initializes clock on current time
    public Time() {
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