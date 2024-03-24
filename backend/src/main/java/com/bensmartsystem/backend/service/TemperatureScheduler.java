package com.bensmartsystem.backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.List;

import com.bensmartsystem.backend.model.Zone;

@Component
public class TemperatureScheduler {

    private final ZoneService zoneService;

    public TemperatureScheduler(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @Scheduled(fixedRate = 60000) // Every min
    public void updateZoneTemperatures() {
        System.out.println("Updating zone temperatures...");
        String currentTimeSlot = determineCurrentTimeSlot();
        List<Zone> allZones = zoneService.findAllZones();
        System.out.println("Zone 1: " + allZones.size());

        allZones.stream()
            .filter(zone -> zone.getTemperatureForTime(currentTimeSlot) != Zone.defaultTemperature) // Filter zones with specific schedules
            .forEach(zone -> {
                double scheduledTemperature = zone.getTemperatureForTime(currentTimeSlot);
                System.out.println("Scheduled temperature for zone " + zone.getId() + " is " + scheduledTemperature);
                if (zoneService.applyTemperatureToZone(zone.getId(), scheduledTemperature)) { 
                    System.out.println("Updated zone " + zone.getId() + " to temperature " + scheduledTemperature);
                }
            });
    }

    private String determineCurrentTimeSlot() {
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(5, 0)) && now.isBefore(LocalTime.of(12, 0))) {
            return "morning";
        } else if (now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(17, 0))) {
            return "afternoon";
        } else {
            return "evening";
        }
    }
}
