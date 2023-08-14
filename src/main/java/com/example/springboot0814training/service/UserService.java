package com.example.springboot0814training.service;

import com.example.springboot0814training.dto.UserLoginRequest;
import com.example.springboot0814training.dto.UserRegisterRequest;
import com.example.springboot0814training.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
}
