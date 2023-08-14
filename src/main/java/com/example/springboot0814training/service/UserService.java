package com.example.springboot0814training.service;

import com.example.springboot0814training.dto.UserLoginRequest;
import com.example.springboot0814training.dto.UserRegisterQuest;
import com.example.springboot0814training.model.User;

public interface UserService {
    Integer register(UserRegisterQuest userRegisterQuest);

    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
}
