package com.project.socc.controllers;

import org.springframework.web.bind.annotation.*;

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
