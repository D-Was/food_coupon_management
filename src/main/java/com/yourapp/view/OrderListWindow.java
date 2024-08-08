package com.yourapp.view;

import com.yourapp.model.FoodItem;
import com.yourapp.model.OrderDetails;
import com.yourapp.util.DatabaseUtil;
import com.yourapp.util.ReceiptGenerator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Cursor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderListWindow {

    public void start(Stage primaryStage, String actionType) {
        // Create a new VBox for the content
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        try {
            // Fetch and display orders
            List<OrderDetails> orders = DatabaseUtil.getAllOrders();
            for (OrderDetails orderDetails : orders) {
                int orderId = orderDetails.getOrderId();
                Map<FoodItem, Integer> foodItems = orderDetails.getFoodItems();

                // Create order header
                Label orderHeader = new Label("Order Number: " + orderId);
                orderHeader.setFont(new Font("Arial", 16));
                orderHeader.setStyle("-fx-font-weight: bold;");
                orderHeader.setPadding(new Insets(10, 0, 10, 0));
                orderHeader.setAlignment(Pos.CENTER);
                orderHeader.setPrefWidth(Double.MAX_VALUE);
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

                // Create items box
                VBox itemsBox = new VBox();
                itemsBox.setSpacing(5);
                itemsBox.setPadding(new Insets(10));
                itemsBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #f5f5f5;");
                itemsBox.setPrefWidth(Double.MAX_VALUE);

                for (Map.Entry<FoodItem, Integer> entry : foodItems.entrySet()) {
                    FoodItem foodItem = entry.getKey();
                    int quantity = entry.getValue();

                    HBox itemBox = new HBox();
                    itemBox.setSpacing(10);
                    itemBox.setAlignment(Pos.CENTER_LEFT);
                    itemBox.setPrefWidth(Double.MAX_VALUE);

                    Label itemName = new Label(foodItem.getName());
                    itemName.setFont(new Font("Arial", 14));
                    itemName.setPrefWidth(200);

                    Label itemQuantity = new Label("qty: " + quantity);
                    itemQuantity.setFont(new Font("Arial", 14));

                    itemBox.getChildren().addAll(itemName, itemQuantity);
                    itemsBox.getChildren().add(itemBox);
                }

                vbox.getChildren().addAll(orderHeader, itemsBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create and add bottom box
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));

        Button backButton = new Button("Back");
        backButton.setFont(new Font("Arial", 20));
        backButton.setPrefWidth(150);
        backButton.setOnAction(event -> {
            MainMenu mainMenu = new MainMenu();
            try {
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        bottomBox.getChildren().add(backButton);
        vbox.getChildren().add(bottomBox);

        // Create and set ScrollPane
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        StackPane root = new StackPane();
        root.getChildren().add(scrollPane);

        // Create and set Scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Order List");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        // Force UI to update
        primaryStage.getScene().getRoot().requestLayout();
        primaryStage.getScene().getRoot().applyCss();
    }

    private void showDeleteConfirmation(Stage owner, int orderId) {
        Stage confirmationStage = new Stage();
        confirmationStage.initModality(Modality.WINDOW_MODAL);
        confirmationStage.initOwner(owner);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        Label confirmationMessage = new Label("Are you sure you want to delete order " + orderId + "?");
        vbox.getChildren().add(confirmationMessage);

        HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            try {
                DatabaseUtil.deleteOrder(orderId);
                ReceiptGenerator.deleteReceipt(orderId);
                confirmationStage.close();
                MainMenu m = new MainMenu();
                m.start(owner);
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
