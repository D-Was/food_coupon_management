package com.yourapp.view;

import com.yourapp.model.FoodItem;
import com.yourapp.model.OrderDetails;
import com.yourapp.util.DatabaseUtil;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Cursor;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.*;

public class OrderListWindow {

    public void start(Stage primaryStage, String actionType) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        try {
            List<OrderDetails> orders = DatabaseUtil.getAllOrders();
            for (OrderDetails orderDetails : orders) {
                int orderId = orderDetails.getOrderId();
                Map<FoodItem, Integer> foodItems = orderDetails.getFoodItems();

                // Create the header for each order
                Label orderHeader = new Label("Order Number: " + orderId);
                orderHeader.setStyle("-fx-font-weight: bold;");
                orderHeader.setCursor(Cursor.HAND);
                orderHeader.setOnMouseClicked(event -> {
                    if (actionType.equals("edit")) {          
                        EditSingleCoupon editSingleCoupon = new EditSingleCoupon(orderDetails);
                        try {
                            editSingleCoupon.start(primaryStage);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (actionType.equals("delete")) {
                        showDeleteConfirmation(primaryStage, orderId);
                    }
                });

                // Create a VBox to hold the items for this order
                VBox itemsBox = new VBox();
                itemsBox.setSpacing(5);
                for (Map.Entry<FoodItem, Integer> entry : foodItems.entrySet()) {
                    FoodItem foodItem = entry.getKey();
                    int quantity = entry.getValue();
                    Label itemLabel = new Label("   " + foodItem.getName() + " qty: " + quantity);
                    itemsBox.getChildren().add(itemLabel);
                }

                // Add the order header and items to the main VBox
                vbox.getChildren().addAll(orderHeader, itemsBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message to the user)
        }

        // Create a HBox for the button
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.setStyle("-fx-alignment: center;"); // Center align content in the HBox

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            MainMenu mainMenu = new MainMenu();
            try {
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        bottomBox.getChildren().add(backButton);

        // Add the bottomBox to the main VBox
        vbox.getChildren().add(bottomBox);

        ScrollPane scrollPane = new ScrollPane(vbox);
        Scene scene = new Scene(scrollPane, 400, 600);

        primaryStage.setTitle("Order List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showDeleteConfirmation(Stage owner, int orderId) {
        Stage confirmationStage = new Stage();
        confirmationStage.initModality(Modality.WINDOW_MODAL);
        confirmationStage.initOwner(owner);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10;");

        Label confirmationMessage = new Label("Are you sure you want to delete order " + orderId + "?");
        vbox.getChildren().add(confirmationMessage);

        HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(10);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            try {
                DatabaseUtil.deleteOrder(orderId);
                confirmationStage.close();
                start(owner, "delete");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> confirmationStage.close());

        buttonsBox.getChildren().addAll(deleteButton, backButton);
        vbox.getChildren().add(buttonsBox);

        Scene scene = new Scene(vbox);
        confirmationStage.setScene(scene);
        confirmationStage.setTitle("Confirm Deletion");
        confirmationStage.show();
    }
}
