package com.yourapp.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.yourapp.util.DatabaseUtil;

public class AdminLogin extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Login");

        // Load background image
        Image backgroundImage = null;
        try {
            backgroundImage = new Image(new FileInputStream("./src/main/resources/background3.jpg")); // Adjust path as
                                                                                                      // needed
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(true);

        // Load custom font
        Font lobsterFont = null;
        try {
            lobsterFont = Font.loadFont(new FileInputStream("./src/main/resources/Lobster-Regular.ttf"), 80); // Match
                                                                                                              // MainMenu
                                                                                                              // font
                                                                                                              // size
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Title
        Label title = new Label("Admin Login");
        title.setFont(lobsterFont); // Use the same font as MainMenu
        title.setStyle("-fx-text-fill: black;"); // Match title color to MainMenu

        // UI Elements
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Bold text
        TextField usernameField = new TextField();
        usernameField.setPrefWidth(300); // Adjust width as needed
        usernameField.setMaxWidth(300); // Ensure it doesn't stretch

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Bold text
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(300); // Adjust width as needed
        passwordField.setMaxWidth(300); // Ensure it doesn't stretch

        Button loginButton = new Button("Login");
        loginButton.setFont(new Font("Arial", 20));
        loginButton.setPrefWidth(150);

        Label messageLabel = new Label();
        messageLabel.setFont(new Font("Arial", 14));
        messageLabel.setStyle("-fx-text-fill: red;");

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

        VBox formLayout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton,
                messageLabel);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPadding(new Insets(20));
        formLayout.setSpacing(15);

        // Main layout
        VBox mainLayout = new VBox(20, title, formLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(50, 0, 0, 0)); // Padding at the top to position the title

        // StackPane to layer the background image and the form
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainLayout);

        // Full screen settings
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        // Adjust background image size to full screen
        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
