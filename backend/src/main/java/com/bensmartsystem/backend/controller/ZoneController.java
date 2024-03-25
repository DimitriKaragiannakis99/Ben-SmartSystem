package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Zone;
import com.bensmartsystem.backend.repository.ZoneRepository;
import com.bensmartsystem.backend.model.Room;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneRepository zoneRepository;

    // Constructor injection
    public ZoneController(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @PostMapping
    public ResponseEntity<String> createZone(@RequestBody Map<String, Object> payload) {
        String zoneName = (String) payload.get("name");
        List<String> roomIds = (List<String>) payload.get("rooms");

        List<Room> rooms = new ArrayList<>();
        for (String roomId : roomIds) {
            Room room = RoomController.findRoomById(roomId);
            if (room != null) {
                rooms.add(room);
                System.out.println("Room found with id: " + room);
            } else {
                // Handle the case where the room ID doesn't match any loaded room
                // For example, you could return a bad request response
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Room not found with id: " + roomId);
            }
        }

        Zone zone = new Zone();
        zone.setName(zoneName);
        zone.setRooms(rooms);

        Zone savedZone = zoneRepository.save(zone);
        return ResponseEntity.ok("Zone created with ID: " + savedZone.getId());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllZones() {
        List<Map<String, Object>> zonesWithRoomNames = zoneRepository.findAll().stream().map(zone -> {
            Map<String, Object> zoneInfo = new HashMap<>();
            zoneInfo.put("id", zone.getId());
            zoneInfo.put("name", zone.getName());
            zoneInfo.put("rooms", zone.getRoomNames()); // Here we add the room names to the map
            return zoneInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(zonesWithRoomNames);
    }



    @PostMapping("/{zoneId}/scheduleTemperature")
    public ResponseEntity<?> scheduleTemperature(@PathVariable Long zoneId, @RequestBody Map<String, Object> payload) {
        List<String> timesOfDay = (List<String>) payload.get("timeOfDay");
        Double temperature = ((Number) payload.get("temperature")).doubleValue();

        if (timesOfDay == null || timesOfDay.isEmpty() || temperature == null) {
            return ResponseEntity.badRequest().body("Time of day and temperature are required");
        }

        Zone zone = zoneRepository.findById(zoneId);
        if (zone == null) {
            return ResponseEntity.notFound().build();
        }

        // Apply the temperature for each selected time of day
        timesOfDay.forEach(timeOfDay -> zone.setTemperatureForTime(timeOfDay, temperature));

        zoneRepository.update(zone); 

        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Scheduled temperature set for the selected times");
        return ResponseEntity.ok().body(response);
    }



    @PutMapping("/{zoneId}/temperature")
    public ResponseEntity<?> updateZoneTemperature(@PathVariable Long zoneId,
            @RequestBody Map<String, Double> payload) {
        Double newTemperature = payload.get("temperature");
        if (newTemperature == null) {
            return ResponseEntity.badRequest().body("Temperature is required");
        }

        Zone zone = zoneRepository.findById(zoneId);
        if (zone == null) {
            return ResponseEntity.notFound().build();
        }

        List<Room> rooms = zone.getRooms();
        if (rooms == null || rooms.isEmpty()) {
            return ResponseEntity.badRequest().body("No rooms found in the zone");
        }

        rooms.forEach(room -> {
            zoneRepository.updateRoomTemperature(zoneId, room.getId(), newTemperature);
        });

        return ResponseEntity.ok().body("Temperature updated for all rooms in the zone");
    }

    public boolean applyTemperatureToZone(Zone zone, double temperature) {


        List<Room> rooms = zone.getRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.err.println("No rooms found in zone with ID: " + zone.getId());
            return false;
        }

        // Update the temperature for each room in the zone
        rooms.forEach(room -> room.setTemperature(temperature));


        return true;
    }

    
}
