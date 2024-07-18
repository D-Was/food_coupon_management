package com.yourapp.util;

public class FoodItem {
    private String name;
    private String category;
    private double price;

    public FoodItem(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
}
