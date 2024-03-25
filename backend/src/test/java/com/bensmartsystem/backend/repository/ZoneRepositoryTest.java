package com.bensmartsystem.backend.repository;

import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.Zone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZoneRepositoryTest {

    private ZoneRepository zoneRepository;

    @BeforeEach
    void setUp() {
        zoneRepository = new ZoneRepository();
    }

    @Test
    void saveAndFindById() {
        Zone zone = new Zone();
        zone.setName("Test Zone");

        Zone savedZone = zoneRepository.save(zone);
        assertNotNull(savedZone);
        assertEquals(1, savedZone.getId());

        Zone foundZone = zoneRepository.findById(savedZone.getId());
        assertNotNull(foundZone);
        assertEquals("Test Zone", foundZone.getName());
    }

    @Test
    void findAllZones() {
        Zone zone1 = new Zone();
        zone1.setName("Zone 1");
        Zone zone2 = new Zone();
        zone2.setName("Zone 2");

        zoneRepository.save(zone1);
        zoneRepository.save(zone2);

        List<Zone> zones = zoneRepository.findAll();
        assertNotNull(zones);
        assertEquals(2, zones.size());
    }

    @Test
    void updateRoomTemperature() {
        Zone zone = new Zone();
        Room room = new Room();
        room.setId("room1");
        room.setTemperature(20.0);
        zone.setRooms(List.of(room));

        Zone savedZone = zoneRepository.save(zone);

        boolean result = zoneRepository.updateRoomTemperature(savedZone.getId(), "room1", 25.0);
        assertTrue(result);
        Room updatedRoom = savedZone.getRooms().get(0);
        assertEquals(25.0, updatedRoom.getTemperature());
    }

    @Test
    void updateRoomTemperatureRoomNotFound() {
        Zone zone = new Zone();
        Room room = new Room();
        room.setId("room1");
        zone.setRooms(List.of(room));

        Zone savedZone = zoneRepository.save(zone);

        boolean result = zoneRepository.updateRoomTemperature(savedZone.getId(), "room2", 22.0);
        assertFalse(result);
    }

    @Test
    void updateRoomTemperatureZoneNotFound() {
        boolean result = zoneRepository.updateRoomTemperature(999L, "room1", 22.0);
        assertFalse(result);
    }
}
