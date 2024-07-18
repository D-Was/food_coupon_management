package com.yourapp.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.yourapp.util.DatabaseUtil;

public class AdminLogin extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Login");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (DatabaseUtil.validateAdmin(username, password)) {
                messageLabel.setText("Login successful!");
                // Redirect to Main Menu
                MainMenu mainMenu = new MainMenu();
                try {
                    mainMenu.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                messageLabel.setText("Invalid username or password");
            }
        });

        VBox vbox = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
