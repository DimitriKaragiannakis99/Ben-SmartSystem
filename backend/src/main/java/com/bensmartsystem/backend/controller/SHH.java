package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.ConcreteMediator;
import com.bensmartsystem.backend.model.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@CrossOrigin
@RestController
@RequestMapping("/api/temp")
public class SHH {

    private static final ConcreteMediator m = ConcreteMediator.getInstance();
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
                room.setTemperature(room.getTemperature()+ 5); //Changed value for testing purposes!!!
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
                room.setTemperature(room.getTemperature()- 1); //Changed value for testing purposes!!!
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
            else if(room.getTemperature() >= 135){

                return new ResponseEntity<>("Alert!: Temperature is above 135!", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Temperature is normal", HttpStatus.OK);
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

    /**
     * SHP <-> SHH functionality
     * im going to get an array of all room temps, cross reference it with one 1 every minute and compare
     */
//    public static List<Double> make_room_temp_list(){
//        List<Double> roomTempList = new ArrayList<>();
//        for(Room room : RoomController.getRoomList()){
//            roomTempList.add(room.getTemperature());
//        }
//        return roomTempList;
//    }
//
//    //Limit right now as soon as it detects one room it breaks;
//    public static void room_limit_check(Runnable callback) throws InterruptedException {
//        List<Double> oldRoomTemps = make_room_temp_list();
//        //Wait 1 minute:
//        Timer task = new Timer(true);
//        task.schedule(new TimerTask() {
//            public void run() {
//                List<Double> newRoomTemps = make_room_temp_list();
//                for(int i=0; i < oldRoomTemps.size(); i++){
//                    if (oldRoomTemps.get(i) < newRoomTemps.get(i) - 15) {
//                        task.cancel();
//                        callback.run();
//                        return;
//                    }
//                }
//            }
//        }, 0, 60000);
//    }
//
//    @GetMapping("/check-temp-limit")
//    public ResponseEntity<String> check_limit() throws InterruptedException {
//        room_limit_check(() ->  ResponseEntity.ok("Alert!"));
//        return ResponseEntity.ok("");
//    }
}