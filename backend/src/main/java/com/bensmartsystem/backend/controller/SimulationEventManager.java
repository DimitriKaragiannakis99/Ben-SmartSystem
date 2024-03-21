package com.bensmartsystem.backend.controller;
import com.bensmartsystem.backend.SmartHomeModules; 
import com.bensmartsystem.backend.controller.*;
import lombok.Getter;
import java.util.List;


public class SimulationEventManager {
    // We can have different types of subscribers for different events
    @Getter
    private List<SmartHomeModules> subscribers;

    public void Subscribe(SmartHomeModules s) {
        subscribers.add(s);
    }

    public void Unsubscribe(SmartHomeModules s) {
        subscribers.remove(s);
    }

    // Just an example, later we need more notifies
    public void Notify() {
        for (SmartHomeModules s : subscribers) {
            s.update();
        }
    }

    
}
