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
        rooms.put("room-1", new Room("Living Room", new ArrayList<>(Arrays.asList("user-1")),new ArrayList<>(Arrays.asList("Light","Window","Door"))));
        rooms.put("room-2", new Room("Kitchen", new ArrayList<>(Arrays.asList("user-2")),new ArrayList<>(Arrays.asList("Light","Window","Door"))));
        rooms.put("room-3", new Room("Dining Room", new ArrayList<>(),new ArrayList<>()));
        rooms.put("room-4", new Room("Master Bedroom", new ArrayList<>(),new ArrayList<>()));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        // Return a new ArrayList to avoid exposing the internal storage structure
        return ResponseEntity.ok(new ArrayList<>(rooms.values()));
    }

}

