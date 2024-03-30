package com.bensmartsystem.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class HouseTest {

    private House house;
    private final String houseId = "house123";
    private final String houseName = "MyHouse";
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room("Room1", new ArrayList<>());
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        house = new House(houseName, rooms);
    }

    @Test
    void testHouseCreationWithNameAndRooms() {
        assertNotNull(house.getRooms());
        assertFalse(house.getRooms().isEmpty());
        assertEquals(houseName, house.getName());
        assertFalse(house.isAwayModeOn());
    }

    @Test
    void testHouseCreationWithIdAndName() {
        House houseWithId = new House(houseId, houseName);
        assertEquals(houseId, houseWithId.getId());
        assertEquals(houseName, houseWithId.getName());
        assertTrue(houseWithId.getRooms().isEmpty());
        assertFalse(houseWithId.isAwayModeOn());
    }

    @Test
    void testToggleAwayMode() {
        assertFalse(house.isAwayModeOn());
        
        house.toggleAwayMode();
        assertTrue(house.isAwayModeOn());
        
        house.toggleAwayMode();
        assertFalse(house.isAwayModeOn());
    }

    @Test
    void testAddRoom() {
        Room newRoom = new Room("Room2", new ArrayList<>());
        house.addRoom(newRoom);

        assertTrue(house.getRooms().contains(newRoom));
        assertEquals(2, house.getRooms().size());
    }

    @Test
    void testSetAwayModeOn() {
        house.setAwayModeOn(true);
        assertTrue(house.isAwayModeOn());

        house.setAwayModeOn(false);
        assertFalse(house.isAwayModeOn());
    }
}
