package com.example.springboot0814training.controller;

import com.example.springboot0814training.dto.UserRegisterQuest;
import com.example.springboot0814training.model.User;
import com.example.springboot0814training.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterQuest userRegisterQuest) {
        Integer userId = userService.register(userRegisterQuest);

        User user = userService.getUserById(userId);

        return ResponseEntity.status(201).body(user);
    }
}
