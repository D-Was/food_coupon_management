package com.yourapp.util;

import com.yourapp.model.FoodItem;
import com.yourapp.model.OrderDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static List<OrderDetails> getAllOrders() throws SQLException {
        List<OrderDetails> orders = new ArrayList<>();

        // Adjusted query to fetch order details
        String query = "SELECT order_table.id AS order_id, food_items.id AS food_id, food_items.name AS item_name, order_food.quantity "
                +
                "FROM order_table " +
                "JOIN order_food ON order_table.id = order_food.order_id " +
                "JOIN food_items ON order_food.food_id = food_items.id " +
                "ORDER BY order_table.id, order_food.order_food_id";

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            Map<Integer, Map<FoodItem, Integer>> orderMap = new HashMap<>();

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int foodId = resultSet.getInt("food_id");
                String itemName = resultSet.getString("item_name");
                int quantity = resultSet.getInt("quantity");

                // Assuming FoodItem has a category and price, you may adjust this constructor
                // call
                FoodItem foodItem = new FoodItem(foodId, itemName, null, 0);
                orderMap.computeIfAbsent(orderId, k -> new HashMap<>()).put(foodItem, quantity);
            }

            for (Map.Entry<Integer, Map<FoodItem, Integer>> entry : orderMap.entrySet()) {
                orders.add(new OrderDetails(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static void deleteOrder(int orderId) throws SQLException {
        String query = "DELETE FROM order_table WHERE id = ?";

        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, orderId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order " + orderId + " deleted successfully.");
            } else {
                System.out.println("No order found with ID " + orderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resetAutoIncrement();
    }
    
    private static void resetAutoIncrement() throws SQLException {
        // Execute SQL in separate steps
        try (Connection connection = getConnection()) {
            // 1. Find the maximum ID
            String maxIdQuery = "SELECT COALESCE(MAX(id), 0) AS max_id FROM order_table";
            try (Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(maxIdQuery)) {

                int maxId = 0;
                if (rs.next()) {
                    maxId = rs.getInt("max_id");
                }

                // 2. Reset the AUTO_INCREMENT
                String resetQuery = "ALTER TABLE order_table AUTO_INCREMENT = ?";
                try (PreparedStatement ps = connection.prepareStatement(resetQuery)) {
                    ps.setInt(1, maxId + 1);
                    ps.executeUpdate();
                }
            }
        }
    }

    public static void updateOrder(int orderId, int totalPrice, Map<FoodItem, Integer> orderItems) throws SQLException {
        Connection connection = null;
        PreparedStatement updateOrderStmt = null;
        PreparedStatement deleteOrderItemsStmt = null;
        PreparedStatement insertOrderItemStmt = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            String updateOrderQuery = "UPDATE order_table SET total_price = ? WHERE id = ?";
            updateOrderStmt = connection.prepareStatement(updateOrderQuery);
            updateOrderStmt.setInt(1, totalPrice);
            updateOrderStmt.setInt(2, orderId);
            updateOrderStmt.executeUpdate();

            String deleteOrderItemsQuery = "DELETE FROM order_food WHERE order_id = ?";
            deleteOrderItemsStmt = connection.prepareStatement(deleteOrderItemsQuery);
            deleteOrderItemsStmt.setInt(1, orderId);
            deleteOrderItemsStmt.executeUpdate();

            String insertOrderItemQuery = "INSERT INTO order_food (order_id, food_id, quantity) VALUES (?, ?, ?)";
            insertOrderItemStmt = connection.prepareStatement(insertOrderItemQuery);

            for (Map.Entry<FoodItem, Integer> entry : orderItems.entrySet()) {
                insertOrderItemStmt.setInt(1, orderId);
                insertOrderItemStmt.setInt(2, entry.getKey().getId());
                insertOrderItemStmt.setInt(3, entry.getValue());
                insertOrderItemStmt.addBatch();
            }

            insertOrderItemStmt.executeBatch();

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (updateOrderStmt != null) {
                updateOrderStmt.close();
            }
            if (deleteOrderItemsStmt != null) {
                deleteOrderItemsStmt.close();
            }
            if (insertOrderItemStmt != null) {
                insertOrderItemStmt.close();
            }
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }    

}
