package com.skypro.starbank.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

@org.springframework.stereotype.Repository
public class RepositoryDemo {
    private final JdbcTemplate jdbcTemplate;

    public RepositoryDemo(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getRandomTransactionAmount(){
        Integer result = jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions LIMIT 1", Integer.class);
        return result != null ? result : 0;
    }
}
