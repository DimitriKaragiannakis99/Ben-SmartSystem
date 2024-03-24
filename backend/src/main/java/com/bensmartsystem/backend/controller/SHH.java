package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@CrossOrigin
@RestController
@RequestMapping("/api/temp")
public class SHH {

    int outsideTemp = 18;

    //HAVC Temp algorithm implementation
    public static void heating(Room room){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= room.getTemperature()) {
                    return; // pause the HVAC
                }
                room.setTemperature(room.getTemperature()+ 0.1);
            }
        }, 0, 1000);
    }

    public static void cooling(Room room){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= room.getDesiredTemperature()) {
                    return;
                }
                room.setTemperature(room.getTemperature()- 0.1);
            }
        }, 0, 1000);
    }

    //Should be called continuously to verify whether the temp drops bellow zero
    @GetMapping("/checkTemp")
    public ResponseEntity<String> checkTemp(Room room){
        if (room.getTemperature() <= 0){
            //Must log to console
            return new ResponseEntity<>("Temperature is below zero, pipes may burst!", HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public static void HVAC_on(ArrayList<Room> roomsList){
        for(Room room : roomsList){
            if(room.getTemperature() < room.getDesiredTemperature()){
                heating(room);
            }
            else if(room.getTemperature() > room.getDesiredTemperature()){
                cooling(room);
            }
        }

    }

    @PostMapping("/HVAC-off")
    public ResponseEntity<String> HVAC_off(@RequestBody ArrayList<Room> roomList) {
        for(Room room : roomList){
            Timer timer = new Timer(true);
            if (room.getTemperature() < outsideTemp){
                //Need to heat
                while(room.getTemperature() < outsideTemp){
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if(room.getTemperature() == outsideTemp){
                                return;
                            }
                            room.setTemperature(room.getTemperature()+ 0.05);
                        }
                    }, 0, 1000);
                }
            }
            else if(room.getTemperature() > outsideTemp){
                //Need to cool
               while(room.getTemperature() > outsideTemp){
                   timer.schedule(new TimerTask() {
                       public void run() {
                           if(room.getTemperature() == outsideTemp){
                               return;
                           }
                           room.setTemperature(room.getTemperature()- 0.05);
                       }
                   }, 0, 1000);
               }
            }
        }
        return new ResponseEntity<>("HVAC off", HttpStatus.OK);
    }


}