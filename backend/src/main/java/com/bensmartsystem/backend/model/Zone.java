package com.bensmartsystem.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Zone {
    public final static double defaultTemperature = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Map<String, Double> temperatureSchedule = new HashMap<>();

    // Method to set scheduled temperature
    public void setTemperatureForTime(String timeOfDay, double temperature) {
        this.temperatureSchedule.put(timeOfDay, temperature);
    }
    
    public double getTemperatureForTime(String timeOfDay) {
        return this.temperatureSchedule.getOrDefault(timeOfDay, defaultTemperature); // Define defaultTemperature as needed
    }

    public Map<String, Double> getTemperatureSchedule() {
        return this.temperatureSchedule;
    }
    public void setTemperatureSchedule(Map<String, Double> temperatureSchedule) {
        this.temperatureSchedule = temperatureSchedule;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private List<Room> rooms;

    public List<String> getRoomNames() {
        return rooms.stream().map(Room::getName).collect(Collectors.toList());
    }
}
