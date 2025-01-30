package com.skypro.starbank.repository;

import com.skypro.starbank.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USERS",
                (rs, rowNum) -> new User(rs.getString("id"), rs.getString("username")));
    }

    // Метод для поиска пользователя по имени
    public User findUserByName(String username) {
        String sql = "SELECT * FROM USERS WHERE LOWER(username) = LOWER(?)";

        List<User> users = jdbcTemplate.query(sql, new Object[]{username},
                (rs, rowNum) -> new User(
                        rs.getString("id"),  // если id - число
                        rs.getString("username")
                )
        );

        logger.debug("Найденные пользователи: {}", users);
        return users.isEmpty() ? null : users.get(0);
    }
}