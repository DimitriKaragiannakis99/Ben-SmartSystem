package com.bensmartsystem.backend.model;

import lombok.Setter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Room {

    private String id;
    private String name;
    // TODO: Change this to User Object
    private List<String> users;
    private List<String> roomComponents;
    private boolean isWindowBlocked;
    private boolean isLightOn;
    private boolean isWindowOpen;
    private boolean isDoorOpen;
    private boolean isAutoLightOn;
    private boolean isAutoLockOn;

    public Room(String name, List<String> roomComponents) {
        this.id = UUID.randomUUID().toString();
        this.users = new ArrayList<>();
        this.name = name;
        this.roomComponents = roomComponents;
        this.isWindowBlocked = false;
        this.isLightOn = false;
        this.isWindowOpen = false;
        this.isDoorOpen = false;
        this.isAutoLightOn = false;
        this.isAutoLockOn = false;
    }

    public Room(String name, List<String> roomComponents, List<String> users) {
        this.id = UUID.randomUUID().toString();
        this.users = users;
        this.name = name;
        this.roomComponents = roomComponents;
        this.isWindowBlocked = false;
        this.isLightOn = false;
        this.isWindowOpen = false;
        this.isDoorOpen = false;
    }

    public Room(String name, List<String> users, List<String> roomComponents, boolean isWindowBlocked,
            boolean isLightOn, boolean isWindowOpen, boolean isDoorOpen,boolean isAutoLightOn,boolean isAutoLockOn) {
        this.id = UUID.randomUUID().toString();
        this.users = users;
        this.name = name;
        this.roomComponents = roomComponents;
        this.isWindowBlocked = isWindowBlocked;
        this.isLightOn = isLightOn;
        this.isWindowOpen = isWindowOpen;
        this.isDoorOpen = isDoorOpen;
        this.isAutoLightOn = isAutoLightOn;
        this.isAutoLockOn = isAutoLockOn;
    }

    // Use to debug:
    public String toString() {
        StringBuilder roomInfo = new StringBuilder();
        roomInfo.append(this.name).append("\n");
        roomInfo.append("Components = { ");
        for (String s : this.roomComponents) {
            roomInfo.append(s).append(" ");
        }
        roomInfo.append("}");
        return roomInfo.toString();
    }

    public void updateFrom(Room incomingRoom) {
        this.name = incomingRoom.name;
        this.users = incomingRoom.users;
        this.roomComponents = incomingRoom.roomComponents;
        this.isWindowBlocked = incomingRoom.isWindowBlocked;
        this.isLightOn = incomingRoom.isLightOn;
        this.isWindowOpen = incomingRoom.isWindowOpen;
        this.isDoorOpen = incomingRoom.isDoorOpen;
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public void addUsers(String user) {
        this.users.add(user);
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

    public boolean getIsLightOn() {
        return isLightOn;
    }

    public void setIsLightOn(boolean isLightOn) {
        this.isLightOn = isLightOn;
    }

    public boolean getIsWindowOpen() {
        return isWindowOpen;
    }

    public void setIsWindowOpen(boolean isWindowOpen) {
        this.isWindowOpen = isWindowOpen;
    }

    public boolean getIsDoorOpen() {
        return isDoorOpen;
    }

    public void setIsDoorOpen(boolean isDoorOpen) {
        this.isDoorOpen = isDoorOpen;
    }

    public boolean getIsAutoLightOn(){
        return isAutoLightOn;
    }

    public void setIsAutoLightOn(boolean isAutoLightOn){
        this.isAutoLightOn = isAutoLightOn;

    }

    public boolean getIsAutoLockOn(){
        return isAutoLockOn;
    }

    public void setIsAutoLockOn(boolean isAutoLockOn){
        this.isAutoLockOn = isAutoLockOn;
    }
}
