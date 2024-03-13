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
    // private static final ConcurrentHashMap<String, Room> rooms = new
    // ConcurrentHashMap<>();
    private static final ArrayList<Room> roomList = new ArrayList<>();

    @GetMapping("/rooms")
    public ResponseEntity<ArrayList<Room>> getAllRooms() {
        System.out.println(roomList);
        // Return a new ArrayList to avoid exposing the internal storage structure
        return ResponseEntity.ok(roomList);
    }

    @PostMapping("/saveRooms")
    public ResponseEntity<String> saveRooms(@RequestBody ArrayList<Room> rms) {
        // Iterate through the incoming rooms map

        // Return a response indicating the operation was successful
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
        roomList.clear();

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
                if (parts.length == 3) {
                    String roomName = parts[0].trim();
                    List<String> components = Arrays.asList(parts[1].trim().split(","));
                    List<String> users = Arrays.asList(parts[2].trim().split(","));

                    // Create a new Room instance and add it to the map
                    roomList.add(new Room(roomName, components, users));
                    roomNumber++;
                }
                if (parts.length == 2) {
                    String roomName = parts[0].trim();
                    List<String> components = Arrays.asList(parts[1].trim().split(","));
                    // Create a new Room instance and add it to the map
                    roomList.add(new Room(roomName, components));
                    roomNumber++;
                }
            }
            return new ResponseEntity<>("Room layout uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
