package com.example.shoppingapp.models;

import java.io.Serializable;

public class item implements Serializable {  // Implement Serializable
    private String name;
    private String description;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    private double price;
    private int amount;

    public item(String name, String description, double price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getAmount() { return amount; }
}
