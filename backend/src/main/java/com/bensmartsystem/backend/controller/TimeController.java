package com.bensmartsystem.backend.controller;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bensmartsystem.backend.model.Time;

@Getter
@RestController
@CrossOrigin
@RequestMapping("/api/time")
public class TimeController 
{
    private static Time currentTime;

    @Autowired
    public TimeController(Time currentTime) {
        // Hardcoded time data
        currentTime.setHour(12);
        currentTime.setDay(1);
        currentTime.setMonth(1);
        TimeController.currentTime = currentTime;
    }

    @PostMapping("/setTime")
    public void setTime(@RequestBody Time time) {
        //Print to the console the new user added
        System.out.println("New Time set" + time.getHour() + " " + time.getDay() + " " + time.getMonth());
        currentTime = time;
        SimulationEventManager.getInstance().Notify("timeChanged");
    }

    @GetMapping("/getTime")
    public static Time getTime() {
        return currentTime;
    }

    public static Time getCurrentTime(){
        return getTime();
    }


}