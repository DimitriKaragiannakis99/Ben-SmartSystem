package com.bensmartsystem.backend.repository;

import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.Zone;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class ZoneRepository {

    private final List<Zone> zones = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    // Saves a new zone and returns it
    public Zone save(Zone zone) {
        zone.setId(counter.incrementAndGet());
        zones.add(zone);
        return zone;
    }

    public boolean update(Zone zone){
        for (Zone z : zones){
            if (z.getId().equals(zone.getId())){
                z.setName(zone.getName());
                z.setRooms(zone.getRooms());
                z.setTemperatureSchedule(zone.getTemperatureSchedule());
                return true;
            }
        }
        return false;
    }

    // Returns all zones
    public List<Zone> findAll() {
        return zones;
    }

    // Find a zone by ID
    public Zone findById(Long id) {
        return zones.stream()
                .filter(zone -> zone.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Update a room's temperature within a zone
    public boolean updateRoomTemperature(Long zoneId, String roomId, double newTemperature) {
        Zone zone = findById(zoneId);
        if (zone == null) {
            return false;
        }

        boolean updated = false;
        for (Room room : zone.getRooms()) {
            if (room.getId().equals(roomId)) {
                room.setTemperature(newTemperature);
                updated = true;
                break;
            }
        }

        return updated;
    }

}
