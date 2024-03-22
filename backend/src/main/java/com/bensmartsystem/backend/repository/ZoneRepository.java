package com.bensmartsystem.backend.repository;

import com.bensmartsystem.backend.model.Zone;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ZoneRepository {

    private final List<Zone> zones = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    // Saves a new zone and returns it
    public Zone save(Zone zone) {
        zone.setId(counter.incrementAndGet());
        zones.add(zone);
        return zone;
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

}
