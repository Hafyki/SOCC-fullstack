package com.project.socc.controllers;

import com.project.socc.dtos.UserRequestDTO;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> postUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        // Retorna o objeto já salvo no banco de dados
        User user = userService.addUser(userRequestDTO.toEntity());

        // Constrói a URI (endereço) do recurso recém-criado
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest() // Pega a URL da requisição atual (http://localhost:8080/users)
                .path("/{id}") // Adiciona o path (http://localhost:8080/users/{id})
                .buildAndExpand(user.getId()) // Substitui o {id} pelo ID do novo usuário
                .toUri(); // Converte em um objeto URI

        // Retorna status 201 Created com o Location no header apontando para o endereço do novo recurso e o usuário criado
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.status(404)
                        .body((User) Map.of("Erro", "Usuário com ID " + id + " não encontrado")));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> fields
    ) {
        User updatedUser = userService.updateUser(id, fields);
        return ResponseEntity.ok(updatedUser);
    }
}