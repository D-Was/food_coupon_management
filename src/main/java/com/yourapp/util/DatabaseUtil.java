package com.yourapp.util;

import com.yourapp.model.FoodItem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String query = "SELECT id, name, category, price FROM food_items";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                foodItems.add(new FoodItem(id, name, category, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foodItems;
    }

    public static int insertOrder(int totalPrice) throws SQLException {
        String insertOrderSQL = "INSERT INTO order_table (total_price, status) VALUES (?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement orderStatement = connection.prepareStatement(insertOrderSQL,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            orderStatement.setInt(1, totalPrice);
            orderStatement.setString(2, "Pending");
            orderStatement.executeUpdate();

            try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    public static void insertOrderItems(int orderId, Map<FoodItem, Integer> orderItems) throws SQLException {
        String insertOrderFoodSQL = "INSERT INTO order_food (order_id, food_id, quantity) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
                PreparedStatement orderFoodStatement = connection.prepareStatement(insertOrderFoodSQL)) {
            for (Map.Entry<FoodItem, Integer> entry : orderItems.entrySet()) {
                orderFoodStatement.setInt(1, orderId);
                orderFoodStatement.setInt(2, entry.getKey().getId());
                orderFoodStatement.setInt(3, entry.getValue());
                orderFoodStatement.addBatch();
            }
            orderFoodStatement.executeBatch();
        }
    }
}
