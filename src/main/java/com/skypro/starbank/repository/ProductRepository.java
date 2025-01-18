package com.skypro.starbank.repository;

import com.skypro.starbank.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAllProducts() {
        return jdbcTemplate.query("SELECT * FROM PRODUCTS",
                (rs, rowNum) -> new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("description")));
    }
}

