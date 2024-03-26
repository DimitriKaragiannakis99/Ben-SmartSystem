package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.Time;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Timer;
import java.util.TimerTask;

@CrossOrigin
@RestController
@RequestMapping("/api/temp")
public class SHH {


    public static double getCurrentOutsideTemp(){
        return Double.parseDouble(OutsideTemperatureController.getCurrentOutTemp());
    }

    @GetMapping("/get-outside-temp")
    public ResponseEntity<String> getOutsideTemp(){
        return ResponseEntity.ok(OutsideTemperatureController.getCurrentOutTemp());
    }

    public static void heating(Room room){
        System.out.printf("Current outside temp: %f%n", getCurrentOutsideTemp());
        Timer task = new Timer(true);
        task.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= room.getDesiredTemperature() || !room.getIsHeaterOn()) {
                    //check if heater is still on if yes -> pause hvac don't turn off
                    if(!room.getIsHeaterOn()){
                        task.cancel();
                        return; // turn off heating
                    }
                    else{
                        //pause heating -> go to outside temp until threshold
                        hvac_paused(room);
                    }
                }
                room.setTemperature(room.getTemperature()+ 0.1);
            }
        }, 0, 1000);
    }

    public static void hvac_off_heat(Room room){
        Timer task = new Timer(true);
        task.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() >= getCurrentOutsideTemp() || room.getIsHeaterOn() || room.getIsAcOn()) {
                    task.cancel();
                    return; // pause the HVAC
                }
                room.setTemperature(room.getTemperature()+ 0.05);
            }
        }, 0, 1000);
    }

    public static void cooling(Room room){
        Timer task = new Timer(true);
        task.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() <= room.getDesiredTemperature() || !room.getIsAcOn()) {
                    if(!room.getIsAcOn()){
                        task.cancel();
                        return; // turn off cooling
                    }
                    else{
                        //pause cooling -> go to outside temp until threshold
                        hvac_paused(room);
                    }
                }
                room.setTemperature(room.getTemperature()- 0.1);
            }
        }, 0, 1000);
    }

    public static void hvac_off_cool(Room room){
        Timer task = new Timer(true);
        task.schedule(new TimerTask() {
            public void run() {
                if (room.getTemperature() <= getCurrentOutsideTemp() || room.getIsHeaterOn() || room.getIsAcOn()) {
                    task.cancel();
                    return;
                }
                room.setTemperature(room.getTemperature()- 0.05);
            }
        }, 0, 1000);
    }

    //Should be called continuously to verify whether the temp drops bellow zero
    @GetMapping("/checkTemp")
    public ResponseEntity<String> checkTemp(){
        for(Room room : RoomController.getRoomList()){
            if (room.getTemperature() <= 0){
                //Must log to console
                return new ResponseEntity<>("Temperature is below zero, pipes may burst!", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("All room temps look ok!", HttpStatus.OK);
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
        if(room.getTemperature() < getCurrentOutsideTemp()){
            room.setIsHeaterOn(false);
            room.setIsAcOn(false);
            hvac_off_heat(room);
            return  ResponseEntity.ok("HVAC was turned off");
        }
        else if(room.getTemperature() > getCurrentOutsideTemp()){
            room.setIsHeaterOn(false);
            room.setIsAcOn(false);
            hvac_off_cool(room);
            return  ResponseEntity.ok("HVAC was turned off");
        }
        return  ResponseEntity.ok("HVAC was not turned off");
    }

    private static boolean heat_or_cool(Room room){
        return room.getTemperature() > getCurrentOutsideTemp();
    }

    public static void hvac_paused(Room room){
        Timer task = new Timer(true);
        task.schedule(new TimerTask() {
            public void run() {
                if (heat_or_cool(room)) {
                    if(room.getTemperature() <= room.getDesiredTemperature()-0.25){
                        task.cancel();
                        return; // stop pause
                    }
                    //cool
                    room.setTemperature(room.getTemperature()- 0.05);
                }
                else {
                    if(room.getTemperature() >= room.getDesiredTemperature()+0.25){
                        task.cancel();
                        return; // stop pause
                    }
                    //heat
                    room.setTemperature(room.getTemperature()+ 0.05);
                }
            }
        }, 0, 1000);
    }
}