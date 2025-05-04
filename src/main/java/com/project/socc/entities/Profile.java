package com.project.socc.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "profile")

public class Profile {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;


}
