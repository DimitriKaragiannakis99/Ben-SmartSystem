package com.bensmartsystem.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bensmartsystem.backend.model.Room;
import com.bensmartsystem.backend.model.User;
import com.bensmartsystem.backend.controller.RoomController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;



@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    @Getter
    private static final List<User> users = new ArrayList<>();

    public UserController() {
        // Hardcoded user data
        users.add(new User("1L", "john_doe", "{\"useDoors\":true,\"useWindows\":false,\"useLights\":false}"));
        users.add(new User("2L", "jane_doe", "{\"useDoors\":false,\"useWindows\":false,\"useLights\":false}"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                //Check first if the get room index is not out of bounds
                String roomName = "No_room_assigned";
                if (RoomController.getRoomList() != null && (user.getRoomIndex() < RoomController.getRoomList().size()) && RoomController.getRoomList().get(user.getRoomIndex())  != null){
                    //Get the room name
                    roomName = RoomController.getRoomList().get(user.getRoomIndex()).getName();
                }
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("username", user.getUsername());
                userData.put("permissions", user.getPermissions());
                userData.put("roomIndex", user.getRoomIndex());
                userData.put("location", roomName);
                
                return ResponseEntity.ok(userData);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
    }

    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        //Print to the console the new user added
        System.out.println("User added: " + user.getUsername());
        users.add(user);
        //Now just add it to the first room 
        RoomController.assignUserToFirstRoom(user);

        // Notify the SimulationEventManager that a new user has been added
        SimulationEventManager.getInstance().Notify("UserAdded");
    }

    @GetMapping("/delete/{id}")
    public void deleteUser(@PathVariable String id) {
       System.out.println("ID to be deleted:  " + id);
       users.removeIf((User user) -> user.getId().equals(id ));

    //    Once it is deleted we just make sure to remove the user from the room
    //    for (User user : users) {
    //         System.out.println("User id: " + user.getId());
    //         if (user.getId().equals(id)) {
    //             users.remove(user);
    //             System.out.println("User deleted: " + id);
    //         }
    //     }
    SimulationEventManager.getInstance().Notify("UserDeleted");
    }
    
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return users;
    }
    
    @PostMapping("/update/{id}")
    public void postMethodName(@PathVariable String id,@RequestBody User entity) 
    {
        System.out.println("ID to be updated:  " + id);
        System.out.println("Entity to be updated:  " + entity);
        
        // First just remove the old one and then just add entity
        users.removeIf((User user) -> user.getId().equals(id ));
        users.add(entity);

        SimulationEventManager.getInstance().Notify("UserUpdated");
    }

}
