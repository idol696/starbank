package com.skypro.starbank.repository;

import com.skypro.starbank.model.Product;
import com.skypro.starbank.model.Transaction;
import com.skypro.starbank.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    // схемы найдены в интернете у похожего проекта - проверили, работает
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> findTransactionsByUserId(String userId) {
        String sql = """
                SELECT t.id, t.amount, t.type, t.date, 
                       u.id AS user_id, u.name AS user_name,
                       p.id AS product_id, p.name AS product_name, p.type AS product_type, p.description AS product_description
                FROM TRANSACTIONS t
                JOIN USERS u ON t.user_id = u.id
                JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.user_id = ?
                """;

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new Transaction(
                        rs.getString("id"),
                        new User(rs.getString("user_id"), rs.getString("user_name")),
                        new Product(
                                rs.getString("product_id"),
                                rs.getString("product_name"),
                                rs.getString("product_type"),
                                rs.getString("product_description")
                        ),
                        rs.getDouble("amount"),
                        rs.getString("type"),
                        rs.getTimestamp("date")
                ));
    }

    public boolean userHasProduct(String userId, String productType) {
        int count = userHasProductCount(userId, productType);
        return userHasProductCount(userId, productType)  > 0;
    }

    public int userHasProductCount(String userId, String productType) {
        String sql = """
                SELECT COUNT(*) FROM TRANSACTIONS t
                JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, productType}, Integer.class);
        return  count != null ? count : 0;
    }

    public double getTotalDeposits(String userId, String productType) {
        String sql = """
                SELECT COALESCE(SUM(t.amount), 0) FROM TRANSACTIONS t
                JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
                """;

        return jdbcTemplate.queryForObject(sql, new Object[]{userId, productType}, Double.class);
    }

    public double getTotalExpenses(String userId, String productType) {
        String sql = """
                SELECT COALESCE(SUM(t.amount), 0) FROM TRANSACTIONS t
                JOIN PRODUCTS p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
                """;

        return jdbcTemplate.queryForObject(sql, new Object[]{userId, productType}, Double.class);
    }
}


