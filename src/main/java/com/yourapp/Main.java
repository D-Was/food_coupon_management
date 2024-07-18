package com.yourapp;

import com.yourapp.view.AdminLogin;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        AdminLogin adminLogin = new AdminLogin();
        adminLogin.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
