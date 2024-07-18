package com.yourapp.view;

import com.yourapp.util.DatabaseUtil;
import com.yourapp.util.FoodItem;
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

import java.util.List;

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

            HBox itemBox = new HBox(10, nameLabel, priceLabel, foodSpinner);
            itemBox.setAlignment(Pos.CENTER_LEFT);

            gridPane.add(nameLabel, 0, row);
            gridPane.add(priceLabel, 1, row);
            gridPane.add(foodSpinner, 2, row);
            row++;
        }

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Handle the submission logic here
            System.out.println("Submit button clicked");
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

        HBox buttonBox = new HBox(10, submitButton, backButton);
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
