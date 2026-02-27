package com.project.ec.controller;

import com.project.ec.entity.User;
import com.project.ec.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return service.createNewUser(user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public List<User> findAklUsers() {
        return service.findAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        service.deleteUser(id);
    }
}
