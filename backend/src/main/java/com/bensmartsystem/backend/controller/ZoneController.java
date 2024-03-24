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

    private final ZoneRepository zoneRepository = new ZoneRepository();

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

    
}
