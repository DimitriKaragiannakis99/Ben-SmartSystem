package com.bensmartsystem.backend;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    private final List<User> users = new ArrayList<>();

    public UserController() {
        // Hardcoded user data
        users.add(new User(1L, "john_doe", "john@example.com"));
        users.add(new User(2L, "jane_doe", "jane@example.com"));
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                System.out.println(user);
                return user;
            }
        }
        return null; // User not found
    }
}
