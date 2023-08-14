package com.example.springboot0814training.dao;

import com.example.springboot0814training.dto.UserRegisterQuest;
import com.example.springboot0814training.model.User;

public interface UserDAO {
    Integer register(UserRegisterQuest userRegisterQuest);

    User getUserById(Integer userId);

    User getUserByEmail(String email);
}
