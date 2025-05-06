package com.project.socc.entities;

import com.project.socc.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Size(min = 5, max = 20)
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 11, max = 11, message = "Phone must have 11 characters")
    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private Integer workLoad; // Só existe para usuários do tipo Docente

    @Column(nullable = false)
    private UserStatus status;
}
