package com.bensmartsystem.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Test
    void testGetAndSetAlertTimer() {
        AlertTimer timer = new AlertTimer(20, house);
        house.setAlertTimer(timer);
        assertEquals(timer, house.getAlertTimer());
        assertEquals(20, house.getAlertTimer().getDelayInSeconds());
    }

    @Test
    void testIsPoliceCalled() {
        assertFalse(house.isPoliceCalled());
        house.setPoliceCalled(true);
        assertTrue(house.isPoliceCalled());
    }

    @Test
    @Timeout(value = 35, unit = TimeUnit.SECONDS)
    void testAlertTimerStartsAndCallsPolice() throws InterruptedException {
        house.getAlertTimer().setDelayInSeconds(5);

        assertFalse(house.isPoliceCalled());
        house.getAlertTimer().startTimer();
        Thread.sleep(6000); // Wait for the timer to finish (slightly longer than the timer delay)
        assertTrue(house.isPoliceCalled());
    }
}
