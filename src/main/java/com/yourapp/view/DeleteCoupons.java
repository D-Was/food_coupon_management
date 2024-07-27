package com.yourapp.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteCoupons extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Delete Coupon");

        // Create and display the OrderListWindow with edit action
        OrderListWindow orderListWindow = new OrderListWindow();
        orderListWindow.start(primaryStage, "delete");
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        // For now, just printing that the EditCoupon window is displayed
        System.out.println("Delete window displayed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
