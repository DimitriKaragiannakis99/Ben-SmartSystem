package com.bensmartsystem.backend.controller;
import org.springframework.web.bind.annotation.*;
import com.bensmartsystem.backend.model.Timer;
@RestController
@CrossOrigin
@RequestMapping("/api/timer")
public class TimerController 
{
    private Timer currentTime = new Timer();

    public TimerController() {

    }

    @PostMapping("/setTimer")
    public void setTimer(@RequestBody Timer timer) {
        //Print to the console the new user added
        System.out.println("New Time set" + timer.getHour() + " " + timer.getMinute() + " " + timer.getSecond());
        currentTime = timer;
        currentTime.tick();
        SimulationEventManager.getInstance().Notify("timeChanged");
    }

    @GetMapping("/getTimer")
    public Timer getTimer() {
        currentTime.tick();
        return this.currentTime;
    }
    
}
