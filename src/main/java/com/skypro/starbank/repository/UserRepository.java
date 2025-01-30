package com.skypro.starbank.repository;

import com.skypro.starbank.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS",
                (rs, rowNum) -> new User(rs.getString("id"), rs.getString("first_name"), rs.getString("last_name")));
    }

    // Метод для поиска пользователя по имени
    public Optional<User> findUserByName(String username) {
        List<User> users = jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM USERS WHERE LOWER(username) = LOWER(?)",
                new Object[]{username},
                (rs, rowNum) -> new User(rs.getString("id"), rs.getString("first_name"), rs.getString("last_name"))
        );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}