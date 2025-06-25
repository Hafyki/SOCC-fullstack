package com.project.socc.controllers;

import com.project.socc.dtos.UserCreateRequestDTO;
import com.project.socc.dtos.UserUpdateRequestDTO;
import com.project.socc.entities.User;
import com.project.socc.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST
     */

    @PostMapping
    public ResponseEntity<User> postUser(@Valid @RequestBody UserCreateRequestDTO userRequest) {

        // Antes de salvar no BD, converte o DTO em uma entidade. Depois que salvar no BD, retorna o usuário que foi salvo
        User user = userService.addUser(userRequest.toEntity());

        // Constrói a URI (endereço) do recurso recém-criado
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest() // Pega a URL da requisição atual (http://localhost:8080/users)
                .path("/{id}") // Adiciona o path (http://localhost:8080/users/{id})
                .buildAndExpand(user.getId()) // Substitui o {id} pelo ID do novo usuário
                .toUri(); // Converte em um objeto URI

        // Retorna status 201 Created com o Location no header apontando para o endereço do novo recurso e o usuário criado
        return ResponseEntity.created(uri).body(user);
    }

    /**
     * GET ALL (com paginação)
     */

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page, // Qual página de dados será retornada (depende do size)
            @RequestParam(defaultValue = "10") int size, // Número de itens por página
            @RequestParam(defaultValue = "name") String sortBy, // Campo pelo qual os resultados serão ordenados
            @RequestParam(defaultValue = "asc") String sortDir // Define a direção da ordenação
    ) {

        // Objeto Sort criado com base na direção e campo de ordenação
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(userService.findUsersPaged(pageable));
    }

    /**
     * GET USER BY ID
     */

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * GET USERS BY USERNAME
     */

    @GetMapping("/search")
    public ResponseEntity<Page<User>> getUsersByUsername(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users = userService.findUsersByUsername(username, pageable);
        return ResponseEntity.ok(users);
    }


    /**
     * PATCH
     */

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequestDTO userRequest
    ) {
        User updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }
}