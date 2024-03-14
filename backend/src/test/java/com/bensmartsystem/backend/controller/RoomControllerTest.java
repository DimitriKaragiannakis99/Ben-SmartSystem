package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomControllerTest {

    private RoomController roomController;

    @BeforeEach
    void setUp() {
        roomController = new RoomController();
    }

    @Test
    void getAllRooms() {
        ResponseEntity<ArrayList<Room>> response = roomController.getAllRooms();
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void saveRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Living Room", Arrays.asList("Light", "Window"), Arrays.asList("User1")));
        ResponseEntity<String> response = roomController.saveRooms(rooms);
        assertEquals("Rooms data saved successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void toggleLight() {
        Room room = new Room("Bedroom", Arrays.asList("Light", "Window"), Arrays.asList("User2"));
        RoomController.getRoomList().add(room);
        ResponseEntity<?> response = roomController.toggleLight(room.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Room updatedRoom = (Room) response.getBody();
        assertNotNull(updatedRoom);
        assertTrue(updatedRoom.getIsLightOn());
    }

    @Test
    void toggleWindow() {
        Room room = new Room("Kitchen", Arrays.asList("Light", "Window"), Arrays.asList("User3"));
        RoomController.getRoomList().add(room);
        ResponseEntity<?> response = roomController.toggleWindow(room.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Room updatedRoom = (Room) response.getBody();
        assertNotNull(updatedRoom);
        assertTrue(updatedRoom.getIsWindowOpen());
    }

    @Test
    void toggleDoor() {
        Room room = new Room("Bathroom", Arrays.asList("Light", "Door"), Arrays.asList("User4"));
        RoomController.getRoomList().add(room);
        ResponseEntity<?> response = roomController.toggleDoor(room.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Room updatedRoom = (Room) response.getBody();
        assertNotNull(updatedRoom);
        assertTrue(updatedRoom.getIsDoorOpen());
    }

   
}
