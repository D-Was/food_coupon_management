package com.yourapp.util;

import com.yourapp.model.FoodItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ReceiptGenerator {

    public static void generateReceipt(int orderId, Map<FoodItem, Integer> orderItems) {
        String directoryPath = "orders";
        File directory = new File(directoryPath);

        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = directoryPath + File.separator + "order_" + orderId + ".svg";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
            writer.write(
                    "<svg baseProfile=\"tiny\" height=\"297mm\" version=\"1.2\" width=\"210mm\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n");
            writer.write("  <defs />\n");
            writer.write(
                    "  <rect fill=\"white\" height=\"287mm\" rx=\"5\" ry=\"5\" stroke=\"black\" width=\"200mm\" x=\"5\" y=\"5\" />\n");
            writer.write(
                    "  <text font-size=\"30px\" font-weight=\"bold\" x=\"50\" y=\"50\">Hattiban Khaja Ghar</text>\n");
            writer.write("  <text font-size=\"18px\" x=\"50\" y=\"90\">Address: Hattiban, Lalitpur</text>\n");
            writer.write("  <text font-size=\"18px\" x=\"50\" y=\"120\">Telp. 11223344</text>\n");
            writer.write(
                    "  <text font-size=\"18px\" x=\"50\" y=\"150\">****************************************</text>\n");
            writer.write("  <text font-size=\"24px\" font-weight=\"bold\" x=\"50\" y=\"180\">Order No: " + orderId
                    + "</text>\n");
            writer.write(
                    "  <text font-size=\"18px\" x=\"50\" y=\"210\">****************************************</text>\n");
            writer.write("  <text font-size=\"22px\" font-weight=\"bold\" x=\"50\" y=\"240\">Food Item</text>\n");
            writer.write("  <text font-size=\"22px\" font-weight=\"bold\" x=\"200\" y=\"240\">Qty</text>\n");
            writer.write("  <text font-size=\"22px\" font-weight=\"bold\" x=\"300\" y=\"240\">Price</text>\n");

            double totalPrice = 0.0;
            int yPosition = 270;

            for (Map.Entry<FoodItem, Integer> entry : orderItems.entrySet()) {
                FoodItem item = entry.getKey();
                int quantity = entry.getValue();
                double itemTotal = item.getPrice() * quantity;
                totalPrice += itemTotal;

                writer.write(
                        "  <text font-size=\"18px\" x=\"50\" y=\"" + yPosition + "\">" + item.getName() + "</text>\n");
                writer.write("  <text font-size=\"18px\" x=\"200\" y=\"" + yPosition + "\">" + quantity + "</text>\n");
                writer.write("  <text font-size=\"18px\" x=\"300\" y=\"" + yPosition + "\">"
                        + String.format("%.2f", itemTotal) + "</text>\n");
                yPosition += 30; // Adjust spacing for each item
            }

            writer.write("  <text font-size=\"18px\" x=\"50\" y=\"" + yPosition
                    + "\">****************************************</text>\n");
            writer.write("  <text font-size=\"22px\" font-weight=\"bold\" x=\"50\" y=\"" + (yPosition + 30)
                    + "\">Total</text>\n");
            writer.write("  <text font-size=\"22px\" font-weight=\"bold\" x=\"300\" y=\"" + (yPosition + 30) + "\">"
                    + String.format("%.2f", totalPrice) + "</text>\n");
            writer.write("  <text font-size=\"18px\" x=\"50\" y=\"" + (yPosition + 60)
                    + "\">****************************************</text>\n");
            writer.write("  <text font-size=\"24px\" font-weight=\"bold\" x=\"50\" y=\"" + (yPosition + 90)
                    + "\">THANK YOU!</text>\n");
            writer.write("</svg>\n");

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void deleteReceipt(int orderId) {
        String fileName = "orders" + File.separator + "order_" + orderId + ".svg";
        File file = new File(fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully: " + fileName);
            } else {
                System.out.println("Failed to delete the file: " + fileName);
            }
        } else {
            System.out.println("File does not exist: " + fileName);
        }
    }
}
