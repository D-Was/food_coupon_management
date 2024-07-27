package com.yourapp.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainMenu extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Menu");

        // Load background image
        Image backgroundImage = null;
        try {
            backgroundImage = new Image(new FileInputStream("./src/main/resources/background2.jpg"));
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
            lobsterFont = Font.loadFont(new FileInputStream("./src/main/resources/Lobster-Regular.ttf"), 80);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Label title = new Label("Main Menu");
        title.setFont(lobsterFont); // Use the custom font for the title

        Button createButton = new Button("Create Coupons");
        createButton.setFont(new Font("Arial", 20));
        createButton.setPrefWidth(300);
        createButton.setOnAction(e -> {
            CreateCoupons createCoupons = new CreateCoupons();
            try {
                createCoupons.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button editButton = new Button("Edit Coupons");
        editButton.setFont(new Font("Arial", 20));
        editButton.setPrefWidth(300);
        editButton.setOnAction(e -> {
            EditCoupons editCoupons = new EditCoupons();
            try {
                editCoupons.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button deleteButton = new Button("Delete Coupons");
        deleteButton.setFont(new Font("Arial", 20));
        deleteButton.setPrefWidth(300);
        deleteButton.setOnAction(e -> {
            DeleteCoupons deleteCoupons = new DeleteCoupons();
            try {
                deleteCoupons.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox buttonBox = new VBox(20, createButton, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Spacer to center the buttons vertically
        Region topSpacer = new Region();
        Region bottomSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        VBox layout = new VBox(10, title, topSpacer, buttonBox, bottomSpacer);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(50, 0, 0, 0));

        // Use StackPane to layer the background image and the layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, layout);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
