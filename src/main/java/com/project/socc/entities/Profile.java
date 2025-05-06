package com.project.socc.entities;


import com.project.socc.enums.ProfileRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Profile {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private ProfileRole role;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean protectedProfile;
}
