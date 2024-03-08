package com.bensmartsystem.backend.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String username;

    @Getter
    @Column(nullable = false)
    private String permissions;

    public User(Long id, String username, String permissions) {
        this.id = id;
        this.username = username;
        this.permissions = permissions;
    }
}
