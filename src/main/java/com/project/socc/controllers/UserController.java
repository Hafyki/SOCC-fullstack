package com.project.socc.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @PostMapping
    public String postUser() {
        return "POST User created";
    }

    // GET

    // PATCH
}
