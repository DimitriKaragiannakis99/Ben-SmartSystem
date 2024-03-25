package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ZoneControllerTest {

    private ZoneController zoneController;
    private RoomController roomController;

    @BeforeEach
    void setUp() {
        roomController = new RoomController();
        
        Room room1 = new Room("Living Room", Arrays.asList("Light", "Window"), Arrays.asList("User1"));
        Room room2 = new Room("Kitchen", Arrays.asList("Light", "Door"), Arrays.asList("User2"));
        RoomController.getRoomList().addAll(Arrays.asList(room1, room2));
    }

    @Test
    void createZone() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Zone 1");
        payload.put("rooms", Arrays.asList(RoomController.getRoomList().get(0).getId(), RoomController.getRoomList().get(1).getId()));

        ResponseEntity<String> response = zoneController.createZone(payload);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Zone created with ID: 1", response.getBody()); // Assuming the ID is auto-incremented starting from 1
    }

    @Test
    void getAllZones() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Zone 1");
        payload.put("rooms", Arrays.asList(RoomController.getRoomList().get(0).getId(), RoomController.getRoomList().get(1).getId()));
        zoneController.createZone(payload);

        ResponseEntity<List<Map<String, Object>>> response = zoneController.getAllZones();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Zone 1", response.getBody().get(0).get("name"));
        assertEquals(Arrays.asList("Living Room", "Kitchen"), response.getBody().get(0).get("rooms"));
    }

    @Test
    void updateZoneTemperature() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Zone 1");
        payload.put("rooms", Arrays.asList(RoomController.getRoomList().get(0).getId(), RoomController.getRoomList().get(1).getId()));
        zoneController.createZone(payload);

        ResponseEntity<?> response = zoneController.updateZoneTemperature(1L, Map.of("temperature", 22.0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Temperature updated for all rooms in the zone", response.getBody());
    }

    @Test
    void updateZoneTemperatureWithoutTemperature() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Zone 1");
        payload.put("rooms", Arrays.asList(RoomController.getRoomList().get(0).getId(), RoomController.getRoomList().get(1).getId()));
        zoneController.createZone(payload);

        ResponseEntity<?> response = zoneController.updateZoneTemperature(1L, new HashMap<>());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateZoneTemperatureZoneNotFound() {
        ResponseEntity<?> response = zoneController.updateZoneTemperature(999L, Map.of("temperature", 22.0));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateZoneTemperatureNoRoomsInZone() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Zone 2");
        payload.put("rooms", Arrays.asList());
        zoneController.createZone(payload);

        ResponseEntity<?> response = zoneController.updateZoneTemperature(2L, Map.of("temperature", 22.0));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
