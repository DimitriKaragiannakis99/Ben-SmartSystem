package com.bensmartsystem.backend.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.Zone;
import com.bensmartsystem.backend.repository.ZoneRepository;
import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    @Autowired
    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public boolean applyTemperatureToZone(Long zoneId, double temperature) {
        Zone zone = zoneRepository.findById(zoneId);
        if (zone == null) {
            System.err.println("Zone not found with ID: " + zoneId);
            return false;
        }

        List<Room> rooms = zone.getRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.err.println("No rooms found in zone with ID: " + zoneId);
            return false;
        }

        // Update the temperature for each room in the zone

        rooms.forEach(room -> room.setTemperature(temperature));
        


        return true;
    }

    public List<Zone> findAllZones() {
        return zoneRepository.findAll();
    }
    
}

