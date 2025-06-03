package com.project.socc.controllers;

import com.project.socc.entities.User;
import com.project.socc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    //POST
    @PostMapping
    public String postUser() {
        return "POST User created";
    }

    //GET ALL
    @GetMapping
    public ResponseEntity<Page<User>>    getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(userService.getAllUsersPaged(pageable));
    }

    //GET ONE
    @GetMapping("/{id}")
    public String getUser(@PathVariable int id) {
        return "GET User with id:" + id + " returned";
    }

    //PATCH
    @PatchMapping("/{id}")
    public String patchUser(@PathVariable int id) {
        return "User with id: " + id + " patched";
    }
}
