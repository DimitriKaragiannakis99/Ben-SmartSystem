package com.bensmartsystem.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.bensmartsystem.backend.model.User;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    private final List<User> users = new ArrayList<>();

    public UserController() {
        // Hardcoded user data
        users.add(new User("1L", "john_doe", "{\"useDoors\":true,\"useWindows\":false,\"useLights\":false}"));
        users.add(new User("2L", "jane_doe", "{\"useDoors\":false,\"useWindows\":false,\"useLights\":false}"));
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                System.out.println(user);
                return user;
            }
        }
        return null; // User not found
    }

    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        //Print to the console the new user added
        System.out.println("User added: " + user.getUsername());
          users.add(user);
    }

    @GetMapping("/delete/{id}")
    public void deleteUser(@PathVariable String id) {
       System.out.println("ID to be deleted:  " + id);
       users.removeIf((User user) -> user.getId().equals(id ));
       
    //    for (User user : users) {
    //         System.out.println("User id: " + user.getId());
    //         if (user.getId().equals(id)) {
    //             users.remove(user);
    //             System.out.println("User deleted: " + id);
    //         }
    //     }

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
    }
    


}
