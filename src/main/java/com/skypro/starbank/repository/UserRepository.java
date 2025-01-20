package com.skypro.starbank.repository;

import com.skypro.starbank.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // оставлено для последующей переработки и доработки
    public List<User> findAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS",
                (rs, rowNum) -> new User(rs.getString("id"), rs.getString("name")));
    }
}

