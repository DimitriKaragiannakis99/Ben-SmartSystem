package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        // Return a new ArrayList to avoid exposing the internal storage structure
        return ResponseEntity.ok(new ArrayList<>(rooms.values()));
    }

    @PostMapping("/saveRooms")
    public ResponseEntity<String> saveRooms(@RequestBody List<Room> rooms) {

        System.out.println(rooms);
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

    // This has the logic for retrieving the .txt file from the front-end, parsing
    // the info and adding it to the hashmap.
    @PostMapping("/uploadRoomLayout")
    public ResponseEntity<String> uploadRoomLayout(@RequestParam("file") MultipartFile file) {
        // Clear existing rooms
        rooms.clear();

        // File was not uploaded correctly
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
        }

        // Read file in and perform tasks:
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int roomNumber = 1;
            while ((line = reader.readLine()) != null) {
                // Split the line by a delimiter to separate room name and components
                String[] parts = line.split(":");
                // Check if the line has both room name and components
                if (parts.length == 2) {
                    String roomName = parts[0].trim();
                    List<String> components = Arrays.asList(parts[1].trim().split(","));
                    // Create a new Room instance and add it to the map
                    rooms.put(String.format("room-%d", roomNumber), new Room(roomName, components));
                    roomNumber++;
                }
            }
            return new ResponseEntity<>("Room layout uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
