package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Timer;
import java.util.TimerTask;

public class SHH {


    //HAVC Temp algorithm implementation
    public static void heating(Room room, int desiredTemperature){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= desiredTemperature) {
                    return; // pause the HVAC
                }
                room.setTemperature(room.getTemperature()+ 0.1);
            }
        }, 0, 1000);
    }

    public static void cooling(Room room, int desiredTemperature){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= desiredTemperature) {
                    return;
                }
                room.setTemperature(room.getTemperature()- 0.1);
            }
        }, 0, 1000);
    }

    //Should be called continuously to verify whether the temp drops bellow zero
    public static void checkTemp(Room room){
        if (room.getTemperature() <= 0){
            //Must log to console
            System.out.println("Temperature is below zero, pipes may burst!");
        }
    }

//    @PostMapping("/HVAC-off")
//    public ResponseEntity<String> HVAC_off(@RequestBody ArrayList<Room> roomList) {
//        for (Room room : roomList) {
//            //Start decreasing room temp to match outside
//        }
//        return
//    }


}
