package com.bensmartsystem.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class User {
    @Getter
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) Removed this since we are not using a DB
    private String id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String username;

    @Getter
    @Setter
    @Column(nullable = false)
    private String permissions;

    public User(String id, String username, String permissions) {
        this.id = id;
        this.username = username;
        this.permissions = permissions;
    }
}
