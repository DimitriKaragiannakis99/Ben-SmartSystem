package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    void toggleHeater() {
        Room room = new Room("Office", Arrays.asList("Light", "Heater"), Arrays.asList("User5"));
        RoomController.getRoomList().add(room);
        ResponseEntity<?> response = roomController.toggleHeater(room.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Room updatedRoom = (Room) response.getBody();
        assertNotNull(updatedRoom);
        assertTrue(updatedRoom.getIsHeaterOn());
    }

    @Test
    void updateRoomTemperature() {
        Room room = new Room("Bedroom", Arrays.asList("Light", "Window"), Arrays.asList("User2"));
        RoomController.getRoomList().add(room);
        ResponseEntity<?> response = roomController.updateRoomTemperature(room.getId(), Map.of("temperature", 25.0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Temperature updated for room with ID: " + room.getId(), response.getBody());
    }

    @Test
    void updateRoomTemperatureWithoutTemperature() {
        Room room = new Room("Living Room", Arrays.asList("Light", "Window"), Arrays.asList("User1"));
        RoomController.getRoomList().add(room);
        ResponseEntity<?> response = roomController.updateRoomTemperature(room.getId(), new HashMap<>());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateRoomTemperatureRoomNotFound() {
        ResponseEntity<?> response = roomController.updateRoomTemperature("nonexistent_room_id",
                Map.of("temperature", 22.0));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void uploadRoomLayout() {
        MultipartFile file = new MockMultipartFile("room_layout.txt",
                "Room1:Light,Window:User1\nRoom2:Light,Door:User2".getBytes(StandardCharsets.UTF_8));
        ResponseEntity<String> response = roomController.uploadRoomLayout(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room layout uploaded successfully", response.getBody());
        assertEquals(2, RoomController.getRoomList().size());
    }

    @Test
    void uploadRoomLayoutEmptyFile() {
        MultipartFile file = new MockMultipartFile("empty_room_layout.txt", new byte[0]);
        ResponseEntity<String> response = roomController.uploadRoomLayout(file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No file uploaded", response.getBody());
    }

    @Test
    void uploadRoomLayoutErrorProcessingFile() {
        MultipartFile file = new MockMultipartFile("room_layout.txt",
                "Room1:Light,Window:User1\nRoom2:Light,Door".getBytes(StandardCharsets.UTF_8));
        ResponseEntity<String> response = roomController.uploadRoomLayout(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room layout uploaded successfully", response.getBody());
        assertEquals(2, RoomController.getRoomList().size());
    }

    @Test
    void checkPermissions() {
        boolean response = RoomController.checkPermissions("{\"doorAccess\":false}", 1);
        assertEquals(false, response);
    }
}
