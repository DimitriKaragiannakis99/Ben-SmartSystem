package com.bensmartsystem.backend.model;
import org.springframework.stereotype.Component;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Component
@Entity
public class Time 
{
    @Getter
    @Setter
    private int hour;
    @Getter
    @Setter
    private int day;
    @Getter
    @Setter
    private int month;


    public Time(int hour, int day, int month) {
        this.hour = hour;
        this.day = day;
        this.month = month;
    }

    public Time() {
        this.hour = 0;
        this.day = 0;
        this.month = 0;
    }

}
