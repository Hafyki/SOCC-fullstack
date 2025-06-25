package com.project.socc.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.socc.dtos.UserCreateRequestDTO;
import com.project.socc.dtos.UserUpdateRequestDTO;
import com.project.socc.entities.User;
import com.project.socc.services.UserService;

import jakarta.validation.Valid;

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
     * GET ONE BY ID
     */

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * GET ALL BY USERNAME
     */

    @GetMapping("/search")
    public ResponseEntity<Page<User>> findUsersByUsername(
        @RequestParam String username,
        @PageableDefault(size = 10, sort = "userName") Pageable pageable) {
        Page<User> page = userService.findUsersByUsername(username, pageable);
        return ResponseEntity.ok(page);
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