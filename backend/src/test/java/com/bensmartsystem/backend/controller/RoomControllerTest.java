package com.bensmartsystem.backend.controller;

import com.bensmartsystem.backend.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RoomControllerTest {

    private RoomController roomController;

    @BeforeEach
    void setUp() {
        roomController = new RoomController();
    }

    @Test
    void testToggleLightWithUnavailableRoom() {
        // Assuming the room "3" does not exist.
        ResponseEntity<?> response = roomController.toggleLight("3");
        assertEquals(404, response.getStatusCodeValue()); // HttpStatus.NOT_FOUND.value() == 404
        assertTrue(response.getBody() instanceof String);
        assertEquals("Room not found with id: 3", response.getBody());
    }

    @Test
    void testToggleWindowWithUnavailableRoom() {
        // Assuming the room "3" does not exist.
        ResponseEntity<?> response = roomController.toggleWindow("3");
        assertEquals(404, response.getStatusCodeValue()); // HttpStatus.NOT_FOUND.value() == 404
        assertTrue(response.getBody() instanceof String);
        assertEquals("Room not found with id: 3", response.getBody());
    }

    @Test
    void testToggleDoorWithUnavailableRoom() {
        // Assuming the room "3" does not exist.
        ResponseEntity<?> response = roomController.toggleDoor("3");
        assertEquals(404, response.getStatusCodeValue()); // HttpStatus.NOT_FOUND.value() == 404
        assertTrue(response.getBody() instanceof String);
        assertEquals("Room not found with id: 3", response.getBody());
    }

    @Test
    void testToggleHVACWithUnavailableRoom() {
        // Assuming the room "3" does not exist.
        ResponseEntity<?> response = roomController.toggleHeater("3");
        assertEquals(404, response.getStatusCodeValue()); // HttpStatus.NOT_FOUND.value() == 404
        assertTrue(response.getBody() instanceof String);
        assertEquals("Room not found with id: 3", response.getBody());
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
        rooms.add(new Room("Living Room", Arrays.asList("Light", "Window")));
        ResponseEntity<String> response = roomController.saveRooms(rooms);
        assertEquals("Rooms data saved successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void uploadRoomLayout() {
        String correctFileContent = "House:MyHouse\nRoom1:Light,Window\nRoom2:Light,Door";
        MultipartFile correctFile = new MockMultipartFile("file", "room_layout.txt", "text/plain", correctFileContent.getBytes());

        ResponseEntity<String> correctResponse = roomController.uploadRoomLayout(correctFile);
        assertEquals(HttpStatus.OK, correctResponse.getStatusCode());
        assertEquals("House and room layout uploaded successfully", correctResponse.getBody());

        String incorrectFileContent = "IncorrectFirstLine\nRoom1:Light,Window\nRoom2:Light,Door";
        MultipartFile incorrectFile = new MockMultipartFile("file", "room_layout.txt", "text/plain", incorrectFileContent.getBytes());

        ResponseEntity<String> incorrectResponse = roomController.uploadRoomLayout(incorrectFile);
        assertEquals(HttpStatus.BAD_REQUEST, incorrectResponse.getStatusCode());
        assertEquals("The first line must start with 'House:' followed by the house name.", incorrectResponse.getBody());
    }

    @Test
    void uploadRoomLayoutEmptyFile() {
        MultipartFile file = new MockMultipartFile("empty_room_layout.txt", new byte[0]);
        ResponseEntity<String> response = roomController.uploadRoomLayout(file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No file uploaded", response.getBody());
    }
}
