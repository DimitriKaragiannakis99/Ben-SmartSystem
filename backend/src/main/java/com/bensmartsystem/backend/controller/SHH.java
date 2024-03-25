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

    static double outsideTemp = 17;

    //HAVC Temp algorithm implementation
    public static void heating(Room room){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= room.getDesiredTemperature() || !room.getIsHeaterOn()) {
                    return; // pause the HVAC
                }
                room.setTemperature(room.getTemperature()+ 0.1);
            }
        }, 0, 1000);
    }

    public static void hvac_off_heat(Room room){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= outsideTemp || room.getIsHeaterOn() || room.getIsAcOn()) {
                    return; // pause the HVAC
                }
                room.setTemperature(room.getTemperature()+ 0.05);
            }
        }, 0, 1000);
    }

    public static void cooling(Room room){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() <= room.getDesiredTemperature() || !room.getIsAcOn()) {
                    return;
                }
                room.setTemperature(room.getTemperature()- 0.1);
            }
        }, 0, 1000);
    }

    public static void hvac_off_cool(Room room){
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() <= outsideTemp || room.getIsHeaterOn() || room.getIsAcOn()) {
                    return;
                }
                room.setTemperature(room.getTemperature()- 0.05);
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
            room.setIsHeaterOn(true);
            heating(room);
            return ResponseEntity.ok("Heater turned on");
        }
        else if(room.getTemperature() > room.getDesiredTemperature()){
            room.setIsAcOn(true);
            cooling(room);
            return ResponseEntity.ok("AC turned on");
        }
        return ResponseEntity.ok("HVAC was not turned on");

    }

    @GetMapping("/HVAC-off")
    public ResponseEntity<String> HVAC_off(@RequestParam String roomID) {
        Room room = RoomController.findRoomById(roomID);
        if(room == null){
            return ResponseEntity.ok("No room was provided");
        }
        if(room.getTemperature() < outsideTemp){
            room.setIsHeaterOn(false);
            room.setIsAcOn(false);
            hvac_off_heat(room);
            return  ResponseEntity.ok("HVAC was turned off");
        }
        else if(room.getTemperature() > outsideTemp){
            room.setIsHeaterOn(false);
            room.setIsAcOn(false);
            hvac_off_cool(room);
            return  ResponseEntity.ok("HVAC was turned off");
        }
        return  ResponseEntity.ok("HVAC was not turned off");
    }


}