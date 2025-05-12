package com.project.socc.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
public class UserController {

    //POST
    @PostMapping
    public String postUser() {
        return "POST User created";
    }

    //GET ALL
    @GetMapping
    public String getAllUsers() { return "GET Users returned"; }

    //GET ONE
    @GetMapping("{/id}")
    public String getUser(@PathVariable int id) { return "GET User with id:" + id +" returned"; }

    //PATCH
    @PatchMapping("/{id}")
    public String patchUser(@PathVariable int id) { return "User with id: " + id + " patched"; }
}
