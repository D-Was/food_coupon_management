package com.yourapp.view;

import com.yourapp.model.FoodItem;
import com.yourapp.model.OrderDetails;
import com.yourapp.util.DatabaseUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditSingleCoupon extends Application {

    private OrderDetails orderDetails;

    public EditSingleCoupon(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edit Coupons");

        Image backgroundImage = null;
        try {
            backgroundImage = new Image(new FileInputStream("./src/main/resources/burgir.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(1920); // Adjust size as needed
        backgroundImageView.setFitHeight(1080);
        backgroundImageView.setPreserveRatio(true);

        // Load custom font
        Font lobsterFont = null;
        try {
            lobsterFont = Font.loadFont(new FileInputStream("./src/main/resources/Lobster-Regular.ttf"), 36);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Create the title label with Lobster font
        Label titleLabel = new Label("Edit Coupons");
        titleLabel.setFont(lobsterFont); // Use the custom font for the title
        titleLabel.setStyle("-fx-text-fill: #333;"); // Optional: set a text color

        List<FoodItem> foodItems = DatabaseUtil.getFoodItems();

        GridPane gridPane = new GridPane();
        gridPane.setVgap(20); // Increase vertical gap
        gridPane.setHgap(20); // Increase horizontal gap
        gridPane.setPadding(new Insets(30)); // Increase padding
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

        Map<FoodItem, Integer> existingOrderItems = orderDetails.getFoodItems();

        for (FoodItem foodItem : foodItems) {
            if (!foodItem.getCategory().equals(currentCategory)) {
                currentCategory = foodItem.getCategory();
                Label categoryLabel = new Label(currentCategory);
                categoryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Increase font size for
                                                                                       // category
                gridPane.add(categoryLabel, 0, row, 3, 1); // Span 3 columns for category label
                row++;
            }

            Label nameLabel = new Label(foodItem.getName());
            nameLabel.setStyle("-fx-font-size: 16px;"); // Increase font size for item name
            Label priceLabel = new Label("Rs " + foodItem.getPrice() + ":");
            priceLabel.setStyle("-fx-font-size: 16px;"); // Increase font size for price

            // Find the corresponding FoodItem in existingOrderItems by matching id
            int initialQuantity = 0;
            for (Map.Entry<FoodItem, Integer> entry : existingOrderItems.entrySet()) {
                if (entry.getKey().getId() == foodItem.getId()) {
                    initialQuantity = entry.getValue();
                    break;
                }
            }

            Spinner<Integer> foodSpinner = new Spinner<>(0, 10, initialQuantity);
            foodSpinner.setStyle("-fx-font-size: 16px;"); // Increase font size for spinner
            foodSpinner.setMaxWidth(100); // Increase width of spinner
            spinnerMap.put(foodItem, foodSpinner);

            HBox itemBox = new HBox(20, nameLabel, priceLabel, foodSpinner); // Increase spacing between elements
            itemBox.setAlignment(Pos.CENTER_LEFT);

            gridPane.add(nameLabel, 0, row);
            gridPane.add(priceLabel, 1, row);
            gridPane.add(foodSpinner, 2, row);
            row++;
        }

        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 20;"); // Increase font size and padding for button
        editButton.setOnAction(e -> {
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
                    DatabaseUtil.updateOrder(orderDetails.getOrderId(), totalPrice, orderItems);
                    System.out.println("Order " + orderDetails.getOrderId() + " updated.");
                    mainMenu.start(primaryStage);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Please select at least one item.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 20;"); // Increase font size and padding for button
        backButton.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu();
            try {
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(20, backButton, editButton); // Increase spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(30, titleLabel, gridPane, buttonBox); // Add title to VBox
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setPadding(new Insets(30)); // Increase padding around VBox
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        // Use StackPane to layer the background image and the layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainBox);

        Scene scene = new Scene(root, 800, 800); // Adjust scene size if necessary
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
