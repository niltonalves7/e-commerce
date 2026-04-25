package com.project.ecomerce.domain.user.controller;

import com.project.ecomerce.domain.user.dto.request.RegisterUserRequestDTO;
import com.project.ecomerce.domain.user.dto.response.RegisterUserResponseDTO;
import com.project.ecomerce.domain.user.service.UserService;
import com.project.ecomerce.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserServiceImpl clientService) {
        this.userService = clientService;
    }

    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> createUser(@RequestBody @Valid RegisterUserRequestDTO clientDto) {

        RegisterUserResponseDTO response = userService.createUser(clientDto);
        return ResponseEntity
                .created(URI.create("/users/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RegisterUserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterUserResponseDTO> getUserById(@PathVariable UUID id) {
                return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisterUserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody @Valid RegisterUserRequestDTO clientDto) {
        return ResponseEntity.ok(userService.updateUser(id, clientDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}