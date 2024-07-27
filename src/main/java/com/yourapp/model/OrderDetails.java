package com.yourapp.model;

import java.util.Map;

public class OrderDetails {
    private int orderId;
    private Map<FoodItem, Integer> foodItems; // Map of food items and their quantities

    public OrderDetails(int orderId, Map<FoodItem, Integer> foodItems) {
        this.orderId = orderId;
        this.foodItems = foodItems;
    }

    public int getOrderId() {
        return orderId;
    }

    public Map<FoodItem, Integer> getFoodItems() {
        return foodItems;
    }
}
