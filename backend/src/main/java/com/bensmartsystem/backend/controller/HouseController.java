package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.ConcreteMediator;
import com.bensmartsystem.backend.model.House;
import com.bensmartsystem.backend.model.Room;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/house")
public class HouseController {

    private static final ConcreteMediator m = ConcreteMediator.getInstance();
    private static House house;

    @PostMapping("/create")
    public ResponseEntity<String> createHouse(@RequestParam String name, @RequestBody List<Room> rooms) {
        house = new House(name, rooms);
        return ResponseEntity.ok("House created successfully with name: " + name);
    }

    @GetMapping("/awayMode")
    public ResponseEntity<?> getAwayMode() {
        if (house == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("House has not been created");
        }
        return ResponseEntity.ok(Map.of("isAwayModeOn", house.isAwayModeOn()));
    }

    @PostMapping("/toggleAwayMode")
    public ResponseEntity<Map<String, Boolean>> toggleAwayMode() {
        if (house == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", false));
        }

        // Implementing the permissions 
         if (!RoomController.checkPermissions("shpAccess", 0)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", false));
         }
          

        //If the it is being set to On then we will notify the mediator
        if (!house.isAwayModeOn()) {
            onAwayMode();
        }

        house.setAwayModeOn(!house.isAwayModeOn());
        SimulationEventManager.getInstance().Notify("AwayModeToggled");
        return ResponseEntity.ok(Map.of("isAwayModeOn", house.isAwayModeOn()));
    }

    @PostMapping("/updateTimer")
    public ResponseEntity<?> updateTimer(@RequestParam int delayInSeconds) {
        if (house == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("House has not been created");
        }
        house.getAlertTimer().setDelayInSeconds(delayInSeconds);
        return ResponseEntity.ok("Timer delay updated to " + delayInSeconds + " seconds");
    }

    @GetMapping("/getTimerDelay")
    public ResponseEntity<?> getTimerDelay() {
        if (house == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("House has not been created");
        }
        return ResponseEntity.ok(Map.of("delayInSeconds", house.getAlertTimer().getDelayInSeconds()));
    }

    @GetMapping("/checkPoliceCalled")
    public ResponseEntity<Boolean> checkPoliceCalled() {
        if (house == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        return ResponseEntity.ok(house.isPoliceCalled());
    }

    public static House getHouse() {
        return house;
    }

    public static void setHouse(House newHouse) {
        house = newHouse;
    }

    public void onAwayMode()
    {
        m.notify(this,"AwayMode");
    }
}
