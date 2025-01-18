package com.skypro.starbank.model;

import java.sql.Timestamp;

public class Transaction {
    private String id;
    private User user;
    private Product product;
    private double amount;
    private String type;
    private Timestamp date;

    public Transaction(String id, User user, Product product, double amount, String type, Timestamp date) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Product getProduct() { return product; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Timestamp getDate() { return date; }

    public String getUserId() { return user.getId(); }
    public String getProductId() { return product.getId(); }
}


