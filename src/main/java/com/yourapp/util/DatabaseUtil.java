package com.yourapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/food_coupon_management";
    private static final String USER = "coupon_admin";
    private static final String PASSWORD = "coupon123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean validateAdmin(String username, String password) {
        String query = "SELECT * FROM Admins WHERE username = ? AND password = ?";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<FoodItem> getFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        String query = "SELECT name, category, price FROM food_items";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                foodItems.add(new FoodItem(name, category, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodItems;
    }
}
