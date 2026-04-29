package jackson.customerordersystem;

import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** javafx gui application for the COS */
public class CustomerOrderSystemGUI extends Application {
  private CustomerOrderSystem system;
  private BorderPane root;
  private Label statusLabel;

  /**
   * starts the application
   *
   * @param args cmd line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * builds and diplays the main gui window
   *
   * @param stage main window
   */
  @Override
  public void start(Stage stage) {
    system = new CustomerOrderSystem();
    seedCatalog();

    root = new BorderPane();
    root.setTop(createHeader());
    root.setLeft(createNavigation());
    root.setCenter(createWelcomeScreen());

    Scene scene = new Scene(root, 900, 600);
    stage.setTitle("Customer Order System");
    stage.setScene(scene);
    stage.show();
  }

  /** products for the catalog */
  private void seedCatalog() {
    system.addProductToCatalog(new Product("CPU", "8-core processor", 329.99, 299.99));
    system.addProductToCatalog(new Product("GPU", "12GB graphics card", 699.99, 649.99));
    system.addProductToCatalog(
        new Product("Motherboard", "ATX motherboard with Wi-Fi", 189.99, 169.99));
    system.addProductToCatalog(new Product("RAM", "32GB DDR5 memory kit", 129.99, 109.99));
    system.addProductToCatalog(new Product("SSD", "1TB NVMe", 99.99, 79.99));
    system.addProductToCatalog(
        new Product("Power Supply", "750W modular power supply", 119.99, 99.99));
    system.addProductToCatalog(new Product("Case", "Mid tower PC case", 109.99, 89.99));
    system.addProductToCatalog(new Product("CPU Cooler", "Tower air cooler", 59.99, 49.99));
  }

  /**
   * create the top header
   *
   * @return header
   */
  private VBox createHeader() {
    Label titleLabel = new Label("Customer Order System");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    statusLabel = new Label();
    updateStatus();

    VBox header = new VBox(8, titleLabel, statusLabel);
    header.setPadding(new Insets(15));
    header.setStyle("-fx-background-color: #ffffff;");

    return header;
  }

  /**
   * creates the navigation buttons on the left hand side
   *
   * @return navigation
   */
  private VBox createNavigation() {
    Button createAccountButton = new Button("Create Account");
    Button logOnButton = new Button("Log On");
    Button logOutButton = new Button("Log Out");
    Button catalogButton = new Button("Browse Catalog");
    Button cartButton = new Button("View Cart");
    Button orderButton = new Button("Make Order");
    Button viewOrdersButton = new Button("View Orders");

    createAccountButton.setOnAction(event -> showCreateAccountScreen());
    logOnButton.setOnAction(event -> showLogOnScreen());
    logOutButton.setOnAction(event -> logOut());
    catalogButton.setOnAction(event -> showCatalogScreen());
    cartButton.setOnAction(event -> showCartScreen());
    orderButton.setOnAction(event -> showMakeOrderScreen());
    viewOrdersButton.setOnAction(event -> showOrdersScreen());

    VBox navigation =
        new VBox(
            10,
            createAccountButton,
            logOnButton,
            logOutButton,
            catalogButton,
            cartButton,
            orderButton,
            viewOrdersButton);
    navigation.setPadding(new Insets(15));
    navigation.setPrefWidth(180);

    for (Node node : navigation.getChildren()) {
      if (node instanceof Button button) {
        button.setMaxWidth(Double.MAX_VALUE);
      }
    }
    return navigation;
  }

  /**
   * creates the welcome screen when opening the gui
   *
   * @return welcome screen
   */
  private Node createWelcomeScreen() {
    Label welcomeLabel = new Label("Welcome to the Customer Order System!");
    Label instructionsLabel = new Label("Use the buttons on the left hand side to select a task.");

    VBox screen = new VBox(12, welcomeLabel, instructionsLabel);
    screen.setPadding(new Insets(20));

    return screen;
  }

  /** displays the create account screen */
  private void showCreateAccountScreen() {
    TextField customerIdField = new TextField();
    PasswordField passwordField = new PasswordField();
    TextField nameField = new TextField();
    TextField addressField = new TextField();
    TextField creditCardField = new TextField();
    ComboBox<String> securityQuestionBox = new ComboBox<>();
    TextField securityAnswerField = new TextField();

    securityQuestionBox.getItems().addAll(system.getSecurityQuestions());
    securityQuestionBox.getSelectionModel().selectFirst();

    GridPane form = createFormGrid();
    form.add(new Label("Customer ID:"), 0, 0);
    form.add(customerIdField, 1, 0);
    form.add(new Label("Password:"), 0, 1);
    form.add(passwordField, 1, 1);
    form.add(new Label("Name:"), 0, 2);
    form.add(nameField, 1, 2);
    form.add(new Label("Address:"), 0, 3);
    form.add(addressField, 1, 3);
    form.add(new Label("Credit Card:"), 0, 4);
    form.add(creditCardField, 1, 4);
    form.add(new Label("Security Question:"), 0, 5);
    form.add(securityQuestionBox, 1, 5);
    form.add(new Label("Security Answer:"), 0, 6);
    form.add(securityAnswerField, 1, 6);

    Button createButton = new Button("Create Account");
    createButton.setOnAction(
        event -> {
          String customerId = customerIdField.getText().trim();
          String password = passwordField.getText().trim();
          String name = nameField.getText().trim();
          String address = addressField.getText().trim();
          String creditCard = creditCardField.getText().trim();
          String securityQuestion = securityQuestionBox.getValue();
          String securityAnswer = securityAnswerField.getText().trim();

          if (customerId.isEmpty()
              || password.isEmpty()
              || name.isEmpty()
              || address.isEmpty()
              || creditCard.isEmpty()
              || securityAnswer.isEmpty()) {
            showError("All fields are required.");
            return;
          }

          if (!system.isCustomerIdAvailable(customerId)) {
            showError("That customer ID already exists.");
            return;
          }

          if (!system.isValidPassword(password)) {
            showError(
                "Password must be at least 6 characters and include a digit, an uppercase letter,"
                    + " and one of the special characters: @ # $ % & *.");
            return;
          }

          boolean created =
              system.createAccount(
                  customerId,
                  password,
                  name,
                  address,
                  creditCard,
                  securityQuestion,
                  securityAnswer);

          if (created) {
            showMessage("Account created successfully.");
            customerIdField.clear();
            passwordField.clear();
            nameField.clear();
            addressField.clear();
            creditCardField.clear();
            securityAnswerField.clear();
          } else {
            showError("Account could not be created. Ensure all fields are correct.");
          }
        });

    VBox screen =
        new VBox(
            12,
            new Label("Create Account"),
            form,
            createButton,
            new Label("Password rule: 6+ characters, digit, uppercase letter, and @ # $ % & *."));
    screen.setPadding(new Insets(20));

    root.setCenter(screen);
  }

  /** displays the log on screen */
  private void showLogOnScreen() {
    TextField customerIdField = new TextField();
    PasswordField passwordField = new PasswordField();

    Button continueButton = new Button("Continue");

    final int[] attempts = {0};

    GridPane form = createFormGrid();
    form.add(new Label("Customer ID:"), 0, 0);
    form.add(customerIdField, 1, 0);
    form.add(new Label("Password:"), 0, 1);
    form.add(passwordField, 1, 1);

    continueButton.setOnAction(
        event -> {
          if (system.getCurrentCustomer() != null) {
            showError("A customer is already logged in.");
            return;
          }

          String customerId = customerIdField.getText().trim();
          String password = passwordField.getText();

          if (customerId.isEmpty() || password.isEmpty()) {
            showError("Customer ID and password are required.");
            return;
          }

          if (!system.customerExists(customerId)) {
            showError("No account found.");
            return;
          }

          attempts[0]++;
          String securityQuestion = system.beginLogOn(customerId, password);

          if (securityQuestion == null) {
            int remaining = 3 - attempts[0];
            if (remaining > 0) {
              showError("Invalid password. Attempts remaining: " + remaining);
            } else {
              showError("Maximum password attempts reached");
              continueButton.setDisable(true);
            }

            return;
          }
          showSecurityQuestionScreen(securityQuestion);
        });

    VBox screen = new VBox(12, new Label("Log On"), form, continueButton);
    screen.setPadding(new Insets(20));

    root.setCenter(screen);
  }

  /**
   * displays the security question step after the customer enters id and password correctly
   *
   * @param securityQuestion question for the pending customer login
   */
  private void showSecurityQuestionScreen(String securityQuestion) {
    TextField securityAnswerField = new TextField();
    Button finishLogOnButton = new Button("Log On");
    Button backButton = new Button("Back to Log On Screen");

    Label questionLabel = new Label("Security question: " + securityQuestion);
    questionLabel.setWrapText(true);

    GridPane form = createFormGrid();
    form.add(new Label("Security Answer:"), 0, 0);
    form.add(securityAnswerField, 1, 0);

    finishLogOnButton.setOnAction(
        event -> {
          String securityAnswer = securityAnswerField.getText().trim();

          if (securityAnswer.isEmpty()) {
            showError("Security answer is required.");
            return;
          }

          if (system.finishLogOn(securityAnswer)) {
            updateStatus();
            showMessage("Welcome, " + system.getCurrentCustomer().getName() + "!");
            showCatalogScreen();
          } else {
            showError("Incorrect security answer.");
            showLogOnScreen();
          }
        });

    backButton.setOnAction(event -> showLogOnScreen());

    VBox screen =
        new VBox(
            12,
            new Label("Security Verification"),
            questionLabel,
            form,
            new HBox(10, finishLogOnButton, backButton));
    screen.setPadding(new Insets(20));

    root.setCenter(screen);
  }

  /** logs out the current customer */
  private void logOut() {
    if (system.getCurrentCustomer() == null) {
      showError("No customer is currently logged in.");
      return;
    }

    system.logOut();
    updateStatus();
    showMessage("Logged out successfully.");
    root.setCenter(createWelcomeScreen());
  }

  /** displays product catalog and add to cart options */
  private void showCatalogScreen() {
    VBox productList = new VBox(12);

    for (Product product : system.getCatalog()) {
      Label productLabel =
          new Label(
              product.getName()
                  + " | "
                  + product.getDescription()
                  + " | Regular: "
                  + product.getRegularPrice()
                  + " | Sale: "
                  + product.getSalePrice());

      TextField quantityField = new TextField();
      quantityField.setPromptText("Quantity");
      quantityField.setMaxWidth(100);

      Button addButton = new Button("Add to Cart");
      addButton.setOnAction(
          event -> {
            int quantity;

            try {
              quantity = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException e) {
              showError("Quantity must be a number.");
              return;
            }

            if (quantity <= 0) {
              showError("Quantity must be greater than zero.");
              return;
            }

            boolean added = system.selectItem(product.getName(), quantity);

            if (added) {
              showMessage(product.getName() + " added to cart.");
              quantityField.clear();
            } else {
              showError("Item could not be added.");
            }
          });
      HBox row = new HBox(10, productLabel, quantityField, addButton);
      productList.getChildren().add(row);
    }
    ScrollPane scrollPane = new ScrollPane(productList);
    scrollPane.setFitToWidth(true);

    VBox screen = new VBox(12, new Label("Browse Catalog"), scrollPane);
    screen.setPadding(new Insets(20));

    root.setCenter(screen);
  }

  /** displays current shopping cart */
  private void showCartScreen() {
    ShoppingCart cart = system.getShoppingCart();
    TextArea cartArea = new TextArea();
    cartArea.setEditable(false);

    if (cart.isEmpty()) {
      cartArea.setText("Your cart is empty.");
    } else {
      StringBuilder text = new StringBuilder();

      for (CartItem item : cart.getItems()) {
        text.append(item.getProduct().getName())
            .append(" | Quantity: ")
            .append(item.getQuantity())
            .append(" | Line Total: ")
            .append(formatMoney(item.getLineTotal()))
            .append("\n");
      }

      text.append("\nSubtotal: ").append(formatMoney(cart.getSubtotal()));
      text.append("\nTax: ").append(formatMoney(cart.getTax()));
      text.append("\nTotal: ").append(formatMoney(cart.getTotal()));

      cartArea.setText(text.toString());
    }
    Button clearCartButton = new Button("Clear Cart");
    clearCartButton.setDisable(cart.isEmpty());
    clearCartButton.setOnAction(
        event -> {
          system.getShoppingCart().clear();
          showMessage("Cart Cleared.");
          showCartScreen();
        });

    VBox screen = new VBox(12, new Label("Shopping Cart"), cartArea, clearCartButton);
    screen.setPadding(new Insets(20));

    root.setCenter(screen);
  }

  /** displays make order screen */
  private void showMakeOrderScreen() {
    if (system.getCurrentCustomer() == null) {
      showError("You must log in before placing an order.");
      return;
    }

    if (system.getShoppingCart().isEmpty()) {
      showError("Your cart is empty.");
      return;
    }

    ComboBox<DeliveryMethod> deliveryBox = new ComboBox<>();
    deliveryBox.getItems().addAll(DeliveryMethod.MAIL, DeliveryMethod.IN_STORE_PICKUP);
    deliveryBox.getSelectionModel().selectFirst();

    Label totalLabel = new Label();
    Button placeOrderButton = new Button("Place Order");

    Runnable updateTotal =
        () -> {
          DeliveryMethod method = deliveryBox.getValue();
          double total = system.getShoppingCart().getTotal() + method.getFee();
          totalLabel.setText("Total including delivery fee: " + formatMoney(total));
        };

    deliveryBox.setOnAction(event -> updateTotal.run());
    updateTotal.run();

    placeOrderButton.setOnAction(
        event -> {
          DeliveryMethod method = deliveryBox.getValue();
          Order order = system.makeOrder(method);

          if (order != null) {
            showMessage(
                "Order placed successfully. \nAuthorization Number: " + order.getAuthNumber());
            showOrdersScreen();
            return;
          }

          TextField newCardField = new TextField();
          newCardField.setPromptText("New 16-digit credit card");

          Button retryButton = new Button("Try New Card");
          Button cancelButton = new Button("Cancel");
          HBox retryButtons = new HBox(10, retryButton, cancelButton);
          VBox retryBox =
              new VBox(
                  10, new Label("Credit card on file was declined"), newCardField, retryButtons);
          retryBox.setPadding(new Insets(20));

          retryButton.setOnAction(
              retryEvent -> {
                String newCard = newCardField.getText().trim();

                if (!newCard.matches("\\d{16}")) {
                  showError("Credit card number must be exactly 16 digits. ");
                  return;
                }

                Order retryOrder = system.makeOrderWithNewCard(method, newCard);

                if (retryOrder != null) {
                  showMessage(
                      "Order placed successfully. \nAuthorization Number: "
                          + retryOrder.getAuthNumber());
                  showOrdersScreen();
                } else {
                  showError("That credit card was also declined.");
                }
              });

          cancelButton.setOnAction(
              cancelEvent -> {
                showMessage("Order cancelled.");
                showCartScreen();
              });

          root.setCenter(retryBox);
        });

    VBox screen =
        new VBox(
            12,
            new Label("Make Order"),
            new Label("Select Delivery Method"),
            deliveryBox,
            totalLabel,
            placeOrderButton);
    screen.setPadding(new Insets(20));

    root.setCenter(screen);
  }

  /** displays customers current order history */
  private void showOrdersScreen() {
    if (system.getCurrentCustomer() == null) {
      showError("You must log in before viewing orders.");
      return;
    }

    List<Order> orders = system.viewOrders();
    TextArea ordersArea = new TextArea();
    ordersArea.setEditable(false);

    if (orders.isEmpty()) {
      ordersArea.setText("No orders found.");
    } else {
      StringBuilder text = new StringBuilder();

      for (int i = 0; i < orders.size(); i++) {
        Order order = orders.get(i);

        text.append("Order ").append(i + 1).append("\n");
        text.append("Order Data: ").append(order.getOrderDate()).append("\n");
        text.append("Delivery Method: ").append(order.getDeliveryMethod()).append("\n");

        for (CartItem item : order.getItems()) {
          text.append(item.getProduct().getName())
              .append(" | Quantity: ")
              .append(item.getQuantity())
              .append(" | Line Total: ")
              .append(formatMoney(item.getLineTotal()))
              .append("\n");
        }

        text.append("Subtotal: ").append(formatMoney(order.getSubtotal())).append("\n");
        text.append("Tax: ").append(formatMoney(order.getTax())).append("\n");
        text.append("Delivery Fee: ").append(formatMoney(order.getDeliveryFee())).append("\n");
        text.append("Total: ").append(formatMoney(order.getTotal())).append("\n");
        text.append("Authorization Number: ").append(order.getAuthNumber()).append("\n");
      }
      ordersArea.setText(text.toString());
    }

    VBox screen = new VBox(12, new Label("View Orders"), ordersArea);
    screen.setPadding(new Insets(20));

    root.setCenter(screen);
  }

  /**
   * helper method, creates a grid for form labels and fields
   *
   * @return returs a configured grid pane
   */
  private GridPane createFormGrid() {
    GridPane form = new GridPane();
    form.setHgap(10);
    form.setVgap(10);
    return form;
  }

  /** helper method, updates the current customer status label */
  private void updateStatus() {
    if (system.getCurrentCustomer() == null) {
      statusLabel.setText("Current customer: none");
    } else {
      statusLabel.setText("Current customer: " + system.getCurrentCustomer().getName());
    }
  }

  /**
   * helper method, shows an info message
   *
   * @param message message to display
   */
  private void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Customer Order System");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * helper method, shows an error message
   *
   * @param message message to display
   */
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Customer Order System");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * helper method, formats numbers as money
   *
   * @param amount amount of money to format
   * @return formatted money string
   */
  private String formatMoney(double amount) {
    return String.format("$%.2f", amount);
  }
}
