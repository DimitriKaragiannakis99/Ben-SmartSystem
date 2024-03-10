package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RoomController {

    // ConcurrentHashMap for thread-safe in-memory storage
    private static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    static {
        // Initialize some rooms with users
        rooms.put("room-1", new Room("room-1", "Living Room", new ArrayList<>(Arrays.asList("user-1")),
                new ArrayList<>(Arrays.asList("Light", "Window", "Door")), true, false, true, true));
        rooms.put("room-2", new Room("room-2", "Kitchen", new ArrayList<>(Arrays.asList("user-2")),
                new ArrayList<>(Arrays.asList("Light", "Window", "Door")), false, true, false, false));
        rooms.put("room-3", new Room("room-3", "Dining Room", new ArrayList<>(), new ArrayList<>(), false, true, true, true));
        rooms.put("room-4", new Room("room-4", "Master Bedroom", new ArrayList<>(), new ArrayList<>(), true, false, false, false));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        // Return a new ArrayList to avoid exposing the internal storage structure
        return ResponseEntity.ok(new ArrayList<>(rooms.values()));
    }

    @PostMapping("/saveRooms")
    public ResponseEntity<String> saveRooms(@RequestBody List<Room> rooms) {
        // TODO: Save the rooms data to a database

        // Return an appropriate response
        return ResponseEntity.ok("Rooms data saved successfully");
    }

    @PostMapping("/toggleLight")
        public ResponseEntity<String> toggleLight(@RequestParam String roomId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.setIsLightOn(!room.getIsLightOn());
            return ResponseEntity.ok("Light toggled successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/toggleWindow")
    public ResponseEntity<String> toggleWindow(@RequestParam String roomId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.setIsWindowOpen(!room.getIsWindowOpen());
            return ResponseEntity.ok("Window toggled successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/toggleDoor")
    public ResponseEntity<String> toggleDoor(@RequestParam String roomId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.setIsDoorOpen(!room.getIsDoorOpen());
            return ResponseEntity.ok("Door toggled successfully");
        }
        return ResponseEntity.notFound().build();
    }

}
