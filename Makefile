# Variables
JAVAC = javac
JAVA = java
SRC_DIR = src/main/java
BUILD_DIR = build
MAIN_CLASS = com.yourapp.Main
JAVAFX_LIB = --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml
MYSQL_JAR = lib/mysql-connector-java-9.0.0.jar  # Ensure this matches the actual JAR file name

# Classpath
CLASSPATH = $(BUILD_DIR):$(MYSQL_JAR):lib/*

# Compile
compile:
	mkdir -p $(BUILD_DIR)
	$(JAVAC) $(JAVAFX_LIB) -d $(BUILD_DIR) \
		$(SRC_DIR)/com/yourapp/Main.java \
		$(SRC_DIR)/com/yourapp/view/AdminLogin.java \
		$(SRC_DIR)/com/yourapp/view/MainMenu.java \
		$(SRC_DIR)/com/yourapp/view/OrderListWindow.java \
		$(SRC_DIR)/com/yourapp/view/EditCoupons.java \
		$(SRC_DIR)/com/yourapp/view/EditSingleCoupon.java \
		$(SRC_DIR)/com/yourapp/view/DeleteCoupons.java \
		$(SRC_DIR)/com/yourapp/view/CreateCoupons.java \
		$(SRC_DIR)/com/yourapp/util/DatabaseUtil.java \
		$(SRC_DIR)/com/yourapp/util/ReceiptGenerator.java \
		$(SRC_DIR)/com/yourapp/model/FoodItem.java \
		$(SRC_DIR)/com/yourapp/model/OrderDetails.java 


# Run
run: compile
	$(JAVA) $(JAVAFX_LIB) -cp "$(CLASSPATH)" $(MAIN_CLASS)

# Clean
clean:
	rm -rf $(BUILD_DIR)/*
