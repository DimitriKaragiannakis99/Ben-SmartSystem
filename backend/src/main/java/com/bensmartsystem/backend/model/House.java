package com.bensmartsystem.backend.model;

import java.util.ArrayList;
import java.util.List;

public class House {

    private String id;
    private String name;
    private List<Room> rooms;
    private boolean isAwayModeOn;
    private AlertTimer alertTimer;
    private boolean isPoliceCalled = false;

    public House(String name, List<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
        this.isAwayModeOn = false;
        this.alertTimer = new AlertTimer(30, this);
    }

    public House(String id, String name) {
        this.id = id;
        this.name = name;
        this.rooms = new ArrayList<>();
        this.isAwayModeOn = false;
        this.alertTimer = new AlertTimer(30, this);
    }

    public void toggleAwayMode() {
        this.isAwayModeOn = !this.isAwayModeOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public boolean isAwayModeOn() {
        return isAwayModeOn;
    }

    public void setAwayModeOn(boolean awayModeOn) {
        isAwayModeOn = awayModeOn;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public AlertTimer getAlertTimer() {
        return alertTimer;
    }
    
    public void setAlertTimer(AlertTimer alertTimer) {
        this.alertTimer = alertTimer;
    }

    public boolean isPoliceCalled() {
        return isPoliceCalled;
    }
    
    public void setPoliceCalled(boolean isPoliceCalled) {
        this.isPoliceCalled = isPoliceCalled;
    }
    

    @Override
    public String toString() {
        return "House{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                ", isAwayModeOn=" + isAwayModeOn +
                '}';
    }
}
