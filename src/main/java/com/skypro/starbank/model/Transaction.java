package com.skypro.starbank.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Transaction {
    private final String id;
    private final User user;
    private final Product product;
    private final double amount;
    private final String type;
    private final Timestamp date;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(getAmount(), that.getAmount()) == 0 && Objects.equals(getId(), that.getId()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getProduct(), that.getProduct()) && Objects.equals(getType(), that.getType()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getProduct(), getAmount(), getType(), getDate());
    }
}


