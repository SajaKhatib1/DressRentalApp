package com.example.dressrentalapp;

import java.io.Serializable;

public class Dress implements Serializable {
    private String name;
    private int price;
    private int imageRes;
    private int quantity;
    private String category;

    public Dress(String name, int price, int imageRes, int quantity, String category) {
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.quantity = quantity;
        this.category = category;
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}