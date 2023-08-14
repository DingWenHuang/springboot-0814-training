package com.example.springboot0814training.dao;

import com.example.springboot0814training.dto.UserRegisterRequest;
import com.example.springboot0814training.model.User;

public interface UserDAO {
    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User getUserByEmail(String email);
}
