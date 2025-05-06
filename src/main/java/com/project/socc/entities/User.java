package com.project.socc.entities;

import com.project.socc.enums.UserStatus;

import java.util.UUID;

public class User {

    private UUID id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private Integer workLoad;
    private UserStatus status;
}
