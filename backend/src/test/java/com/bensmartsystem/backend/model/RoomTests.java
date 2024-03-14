package com.bensmartsystem.backend.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTests {

    @Test
    void testRoomConstructor() {
        List<String> components = Arrays.asList("Light", "Window");
        List<String> users = Arrays.asList("User1", "User2");
        Room room = new Room("Living Room", components, users);

        assertNotNull(room.getId());
        assertEquals("Living Room", room.getName());
        assertEquals(components, room.getRoomComponents());
        assertEquals(users, room.getUsers());
        assertFalse(room.getIsWindowBlocked());
        assertFalse(room.getIsLightOn());
        assertFalse(room.getIsWindowOpen());
        assertFalse(room.getIsDoorOpen());
    }

    @Test
    void testSettersAndGetters() {
        Room room = new Room();
        room.setName("Bedroom");
        room.setUsers(Arrays.asList("User3"));
        room.setRoomComponents(Arrays.asList("Door"));
        room.setIsWindowBlocked(true);
        room.setIsLightOn(true);
        room.setIsWindowOpen(true);
        room.setIsDoorOpen(true);
        room.setIsAutoLightOn(true);
        room.setIsAutoLockOn(true);

        assertEquals("Bedroom", room.getName());
        assertEquals(Arrays.asList("User3"), room.getUsers());
        assertEquals(Arrays.asList("Door"), room.getRoomComponents());
        assertTrue(room.getIsWindowBlocked());
        assertTrue(room.getIsLightOn());
        assertTrue(room.getIsWindowOpen());
        assertTrue(room.getIsDoorOpen());
        assertTrue(room.getIsAutoLightOn());
        assertTrue(room.getIsAutoLockOn());
    }

    @Test
    void testUpdateFrom() {
        Room originalRoom = new Room("Kitchen", Arrays.asList("Fridge"), Arrays.asList("User4"));
        Room updatedRoom = new Room("Updated Kitchen", Arrays.asList("Fridge", "Oven"), Arrays.asList("User4", "User5"));
        updatedRoom.setIsWindowBlocked(true);
        updatedRoom.setIsLightOn(true);
        updatedRoom.setIsWindowOpen(true);
        updatedRoom.setIsDoorOpen(true);

        originalRoom.updateFrom(updatedRoom);

        assertEquals("Updated Kitchen", originalRoom.getName());
        assertEquals(Arrays.asList("Fridge", "Oven"), originalRoom.getRoomComponents());
        assertEquals(Arrays.asList("User4", "User5"), originalRoom.getUsers());
        assertTrue(originalRoom.getIsWindowBlocked());
        assertTrue(originalRoom.getIsLightOn());
        assertTrue(originalRoom.getIsWindowOpen());
        assertTrue(originalRoom.getIsDoorOpen());
    }
}
