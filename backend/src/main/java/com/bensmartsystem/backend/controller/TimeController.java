package com.bensmartsystem.backend.controller;
import org.springframework.web.bind.annotation.*;
import com.bensmartsystem.backend.model.Time;

@RestController
@CrossOrigin
@RequestMapping("/api/time")
public class TimeController 
{
    private Time currentTime = new Time();

    public TimeController() {
    }

    @PostMapping("/setTime")
    public void setTime(@RequestBody Time time) {
        //Print to the console the new user added
        System.out.println("New Time set" + time.getHour() + " " + time.getMinute() + " " + time.getSecond());
        currentTime = time;
        currentTime.tick();
    }

    @GetMapping("/getTime")
    public Time getTime() {
        currentTime.tick(); // start the time
        return this.currentTime;
    }
    
}
