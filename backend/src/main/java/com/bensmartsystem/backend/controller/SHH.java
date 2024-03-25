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

    int outsideTemp = 17;

    //HAVC Temp algorithm implementation
    public static void heating(Room room){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= room.getDesiredTemperature()) {
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
                if (room.getTemperature() <= room.getDesiredTemperature()) {
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

    @GetMapping ("/HVAC-on")
    public ResponseEntity<String> HVAC_on(@RequestParam String roomID){
        Room room = RoomController.findRoomById(roomID);
        if(room == null){
            return ResponseEntity.ok("No room was provided");
        }
        if(room.getTemperature() < room.getDesiredTemperature()){
            heating(room);
            return ResponseEntity.ok("Heater turned on");
        }
        else if(room.getTemperature() > room.getDesiredTemperature()){
            cooling(room);
            return ResponseEntity.ok("AC turned on");
        }
        return ResponseEntity.ok("HVAC was not turned on");

    }

    @PostMapping("/HVAC-off")
    public ResponseEntity<String> HVAC_off(@RequestParam String roomID) {
        Room room = RoomController.findRoomById(roomID);
        if(room == null){
            return ResponseEntity.ok("No room was provided");
        }
        Timer timer = new Timer(true);
        if (room.getTemperature() < outsideTemp){
            //Need to heat
            if(room.getTemperature() < outsideTemp){
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
            if(room.getTemperature() > outsideTemp){
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
        return  ResponseEntity.ok("HVAC was not turned off");
    }


}