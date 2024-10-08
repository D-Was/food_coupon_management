package com.yourapp.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Menu");

        Button createButton = new Button("Create Coupons");
        createButton.setOnAction(e -> {
            // Code to open the Create Coupons screen
            CreateCoupons createCoupons = new CreateCoupons();
            try {
                createCoupons.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button editButton = new Button("Edit Coupons");
        editButton.setOnAction(e -> {
            // Code to open the Edit Coupons screen
            System.out.println("Edit Coupons clicked");
        });

        Button deleteButton = new Button("Delete Coupons");
        deleteButton.setOnAction(e -> {
            // Code to open the Delete Coupons screen
            System.out.println("Delete Coupons clicked");
        });

        VBox vbox = new VBox(10, createButton, editButton, deleteButton);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
