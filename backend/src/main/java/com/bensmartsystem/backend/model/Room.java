package com.bensmartsystem.backend.model;

import lombok.Setter;
import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.UUID;


@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Room {

    private String id;
    private String name;
    private List<String> users;
    private List<String> roomComponents;
    private boolean isWindowBlocked;

    public Room(String name, List<String> roomComponents){
      this.id = UUID.randomUUID().toString();
      this.name = name;
      this.roomComponents = roomComponents;
      this.isWindowBlocked = isWindowBlocked;
       
     }

    public Room(String id, String name, List<String> users, List<String> roomComponents, boolean isWindowBlocked) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.roomComponents = roomComponents;
        this.isWindowBlocked = isWindowBlocked;
    }

    //Use to debug:
    public String toString(){
        StringBuilder roomInfo = new StringBuilder();
        roomInfo.append(this.name).append("\n");
        roomInfo.append("Components = { ");
        for(String s : this.roomComponents){
            roomInfo.append(s).append(" ");
        }
        roomInfo.append("}");
        return roomInfo.toString();
    }

}
