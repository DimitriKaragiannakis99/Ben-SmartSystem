package com.bensmartsystem.backend.controller;
import com.bensmartsystem.backend.SmartHomeModules; 

import lombok.Getter;
import java.util.HashMap;


public class SimulationEventManager {
    // We can have different types of subscribers for different events
    @Getter
    private HashMap<SmartHomeModules,String> subscribers;

    //Use he Singleton design pattern so that only one instance of the SimulationEventManager is created
    private static SimulationEventManager instance = new SimulationEventManager();

    private SimulationEventManager() {
        subscribers = new HashMap<>();
    }

    public static SimulationEventManager getInstance() {
        return instance;
    }

    public void Subscribe(String eventType, SmartHomeModules s) {
        subscribers.put(s,eventType);
    }

    public void Unsubscribe(SmartHomeModules s){
        subscribers.remove(s);
    }

    // Just an example, later we need more notifies
    //This notify is run with any change in the sumulation
    public void Notify(String event) {
        
        for (SmartHomeModules s : subscribers.keySet()) {
            if (subscribers.get(s).equals(event)) {
                s.update();
            }
        }
        System.out.println("Simulation Event Manager Notified " + event);
    }

}
