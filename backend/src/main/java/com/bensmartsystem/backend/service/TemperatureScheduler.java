package com.bensmartsystem.backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.List;

import com.bensmartsystem.backend.model.Zone;
import com.bensmartsystem.backend.model.Time;

@Component
public class TemperatureScheduler {

    private final ZoneService zoneService;
    private final Time time;

    public TemperatureScheduler(ZoneService zoneService, Time time) {
        this.zoneService = zoneService;
        this.time = time;
    }

    @Scheduled(fixedRate = 60000) // Every min
    public void updateZoneTemperatures() {
        String currentTimeSlot = determineCurrentTimeSlot();
        System.out.println("Current time slot: " + currentTimeSlot);
        List<Zone> allZones = zoneService.findAllZones();

        allZones.stream()
            .filter(zone -> zone.getTemperatureForTime(currentTimeSlot) != Zone.defaultTemperature) // Filter zones with specific schedules
            .forEach(zone -> {
                double scheduledTemperature = zone.getTemperatureForTime(currentTimeSlot);
                if (zoneService.applyTemperatureToZone(zone.getId(), scheduledTemperature)) { 
                    System.out.println("Updated zone " + zone.getId() + " to temperature " + scheduledTemperature);
                }
            });
    }

        private String determineCurrentTimeSlot() {
            // Use currentTime, which is your singleton managed by Spring
            int hour = time.getHour();
            if (hour >= 5 && hour < 12) {
                return "morning";
            } else if (hour >= 12 && hour < 17) {
                return "afternoon";
            } else {
                return "evening";
            }
        }
}
