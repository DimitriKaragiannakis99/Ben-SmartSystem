package com.bensmartsystem.backend.controller;
import org.springframework.web.bind.annotation.*;
import com.bensmartsystem.backend.model.Time;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@CrossOrigin
@RequestMapping("/api/time")
public class TimeController 
{
    private Time currentTime = new Time();

    public TimeController() {
        // Hardcoded time data
        currentTime.setHour(12);
        currentTime.setDay(1);
        currentTime.setMonth(1);
    }

    @PostMapping("/setTime")
    public void setTime(@RequestBody Time time) {
        //Print to the console the newuser added
        System.out.println("New Time set" + time.getHour() + " " + time.getDay() + " " + time.getMonth());
        currentTime = time;
    }
    
}
