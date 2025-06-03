package com.project.socc.controllers;

import com.project.socc.entities.User;
import com.project.socc.exceptions.UserNotFoundException;
import com.project.socc.repositories.UserRepository;
import com.project.socc.services.UserService;
import com.project.socc.util.UserModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userModelAssembler = userModelAssembler;
    }


    //POST
    @PostMapping
    public String postUser() {
        //EntityModel<User> entityModel = userModelAssembler.toModel(user);
        //return ResponseEntity
        //                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        //                .body(entityModel);
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
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.status(404)
                        .body((User) Map.of("Erro", "Usuário com ID " + id + " não encontrado")));
    }

    //PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable UUID id, @RequestBody Map<String, Object> fields) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        merge(fields, user);
        user = userRepository.save(user);

        EntityModel<User> entityModel = userModelAssembler.toModel(user);

        return ResponseEntity.ok(entityModel);
    }

    public void merge(Map<String, Object> fields, User user) {
        fields.forEach((propertyName, propertyValue) -> {
            Field field = ReflectionUtils.findField(User.class, propertyName);
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, propertyValue);
        });

    }
}
