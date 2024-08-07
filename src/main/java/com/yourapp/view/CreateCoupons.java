package com.yourapp.view;

import com.yourapp.model.FoodItem;
import com.yourapp.util.DatabaseUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCoupons extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Coupons");

        List<FoodItem> foodItems = DatabaseUtil.getFoodItems();

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20));
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        column2.setHgrow(Priority.NEVER);
        column3.setHgrow(Priority.NEVER);
        gridPane.getColumnConstraints().addAll(column1, column2, column3);

        String currentCategory = "";
        int row = 0;
        Map<FoodItem, Spinner<Integer>> spinnerMap = new HashMap<>();

        for (FoodItem foodItem : foodItems) {
            if (!foodItem.getCategory().equals(currentCategory)) {
                currentCategory = foodItem.getCategory();
                Label categoryLabel = new Label(currentCategory);
                categoryLabel.setStyle("-fx-font-weight: bold;");
                gridPane.add(categoryLabel, 0, row, 3, 1); // Span 3 columns for category label
                row++;
            }

            Label nameLabel = new Label(foodItem.getName());
            Label priceLabel = new Label("Rs " + foodItem.getPrice() + ":");
            Spinner<Integer> foodSpinner = new Spinner<>(0, 10, 0);
            spinnerMap.put(foodItem, foodSpinner);

            HBox itemBox = new HBox(10, nameLabel, priceLabel, foodSpinner);
            itemBox.setAlignment(Pos.CENTER_LEFT);

            gridPane.add(nameLabel, 0, row);
            gridPane.add(priceLabel, 1, row);
            gridPane.add(foodSpinner, 2, row);
            row++;
        }

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            boolean isValidOrder = false;
            int totalPrice = 0;

            Map<FoodItem, Integer> orderItems = new HashMap<>();
            for (Map.Entry<FoodItem, Spinner<Integer>> entry : spinnerMap.entrySet()) {
                int quantity = entry.getValue().getValue();
                if (quantity > 0) {
                    isValidOrder = true;
                    totalPrice += entry.getKey().getPrice() * quantity;
                    orderItems.put(entry.getKey(), quantity);
                }
            }

            if (isValidOrder) {
                try {
                    MainMenu mainMenu = new MainMenu();
                    int orderId = DatabaseUtil.insertOrder(totalPrice);
                    DatabaseUtil.insertOrderItems(orderId, orderItems);
                    System.out.println("Order submitted with token number: " + orderId);
                    mainMenu.start(primaryStage);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Please select at least one item.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu();
            try {
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(10, backButton, submitButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(20, gridPane, buttonBox);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setPadding(new Insets(20));
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        Scene scene = new Scene(mainBox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
