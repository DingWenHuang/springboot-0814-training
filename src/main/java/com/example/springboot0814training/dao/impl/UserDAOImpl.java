package com.example.springboot0814training.dao.impl;

import com.example.springboot0814training.dao.UserDAO;
import com.example.springboot0814training.dto.UserRegisterRequest;
import com.example.springboot0814training.model.User;
import com.example.springboot0814training.rowmapper.UserRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAOImpl implements UserDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO `user` (email, password, created_date, last_modified_date) VALUES (:email, :password, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();

        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM `user` WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> users = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM `user` WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> users = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
}
