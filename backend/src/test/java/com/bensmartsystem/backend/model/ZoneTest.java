package com.bensmartsystem.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZoneTest {
    private Zone zone;
    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        zone = new Zone();
        room1 = new Room();
        room1.setName("Living Room");
        room2 = new Room();
        room2.setName("Kitchen");
        zone.setRooms(Arrays.asList(room1, room2));
    }

    @Test
    void testGetRoomNames() {
        List<String> roomNames = zone.getRoomNames();

        assertEquals(2, roomNames.size());
        assertTrue(roomNames.containsAll(Arrays.asList("Living Room", "Kitchen")));
    }

    @Test
    void testGetId() {
        zone.setId(1L);

        assertEquals(1L, zone.getId());
    }

    @Test
    void testSetAndGetZoneName() {
        String zoneName = "Zone A";
        zone.setName(zoneName);

        assertEquals(zoneName, zone.getName());
    }
}
