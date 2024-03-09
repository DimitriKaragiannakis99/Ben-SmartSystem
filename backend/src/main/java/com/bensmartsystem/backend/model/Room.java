package com.bensmartsystem.backend.model;

import java.util.List;

public class Room {
    private String id;
    private String name;
    private List<String> users;
    private List<String> roomComponents;

    private boolean isWindowBlocked;

    public Room() {
        // Default constructor
    }

    public Room(String id, String name, List<String> users, List<String> roomComponents, boolean isWindowBlocked) {
        this.id = id;
        this.name = name;
        this.users = users;
        this.roomComponents = roomComponents;
        this.isWindowBlocked = isWindowBlocked;
    }

    // Getters and setters for id, name, and users

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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getRoomComponents() {
        return roomComponents;
    }

    public void setRoomComponents(List<String> roomComponents) {
        this.roomComponents = roomComponents;
    }

    public boolean getIsWindowBlocked() {
        return isWindowBlocked;
    }

    public void setIsWindowBlocked(boolean isWindowBlocked) {
        this.isWindowBlocked = isWindowBlocked;
    }
}
