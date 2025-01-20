package com.skypro.starbank.model;

public class Product {
    private String id;
    private String name;
    private String type;
    private String description;

    public Product() {}

    public Product(String id, String name, String type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getDescription() { return description; }
}

