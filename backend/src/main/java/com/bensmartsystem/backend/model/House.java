package com.bensmartsystem.backend.model;

import java.util.ArrayList;
import java.util.List;

public class House {

    private String id;
    private String name;
    private List<Room> rooms;
    private boolean isAwayModeOn;

    public House(String name, List<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
        this.isAwayModeOn = false;
    }

    public House(String id, String name) {
        this.id = id;
        this.name = name;
        this.rooms = new ArrayList<>();
        this.isAwayModeOn = false;
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
