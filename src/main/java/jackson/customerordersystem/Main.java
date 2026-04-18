package jackson.customerordersystem;

import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    CustomerOrderSystem system = new CustomerOrderSystem();

    seedCatalog(system);

    boolean running = true;
    while (running) {
      showMenu(system);
      int choice = readInt(input);

      switch (choice) {
        case 1:
          createAccount(input, system);
          break;
        case 2:
          logOn(input, system);
          break;
        case 3:
          logOut(system);
          break;
        case 4:
          browseAndSelectItems(input, system);
          break;
        case 5:
          showCart(system);
          break;
        case 6:
          makeOrder(input, system);
          break;
        case 7:
          viewOrders(system);
          break;
        case 0:
          running = false;
          System.out.println("Bye!");
          break;
        default:
          System.out.println("Invalid choice.");
      }
    }
  }

  private static void seedCatalog(CustomerOrderSystem system) {
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

  private static void showMenu(CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- Customer Order System ---");

    if (system.getCurrentCustomer() == null) {
      System.out.println("Current customer: none");
    } else {
      System.out.println("Current customer: " + system.getCurrentCustomer().getName());
    }

    System.out.println();
    System.out.println("1. Create Account");
    System.out.println("2. Log On");
    System.out.println("3. Log Out");
    System.out.println("4. Browse Catalog");
    System.out.println("5. View Cart");
    System.out.println("6. Make Order");
    System.out.println("7. View Orders");
    System.out.println("0. Exit");
    System.out.print("Enter choice: ");
  }

  private static void createAccount(Scanner input, CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- Create Account ---");

    String customerId;
    while (true) {
      customerId = readNonBlank(input, "Enter customer ID: ");
      if (system.isCustomerIdAvailable(customerId)) {
        break;
      }
      System.out.println("The customer ID already exists. Enter a different ID.");
    }

    String password;
    while (true) {
      password = readNonBlank(input, "Enter password: ");
      if (system.isValidPassword(password)) {
        break;
      }

      System.out.println(
          "Password must be at least 6 characters and include a digit, an uppercase letter, and one"
              + " of the following: @ # $ % & *.");
    }

    String name = readNonBlank(input, "Enter name: ");
    String address = readNonBlank(input, "Enter address: ");

    String creditCard;
    while (true) {
      creditCard = readNonBlank(input, "Enter a 16-digit credit card number: ");
      if (creditCard.matches("\\d{16}")) {
        break;
      }
      System.out.println("Credit card number must be exactly 16 digits.");
    }

    List<String> securityQuestions = system.getSecurityQuestions();
    System.out.println("Select a security question:");
    for (int i = 0; i < securityQuestions.size(); i++) {
      System.out.println((i + 1) + ". " + securityQuestions.get(i));
    }

    int questionChoice;
    while (true) {
      System.out.print("Enter question number: ");
      questionChoice = readInt(input);
      if (questionChoice >= 1 && questionChoice <= securityQuestions.size()) {
        break;
      }
      System.out.println("Invalid question number.");
    }

    String securityQuestion = securityQuestions.get(questionChoice - 1);
    String securityAnswer = readNonBlank(input, "Enter answer to the security question: ");

    boolean created =
        system.createAccount(
            customerId, password, name, address, creditCard, securityQuestion, securityAnswer);

    if (created) {
      System.out.println("Account created successfully.");
    } else {
      System.out.println("Account could not be created.");
    }
  }

  private static void logOn(Scanner input, CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- Log On ---");

    if (system.getCurrentCustomer() != null) {
      System.out.println("A customer is already logged in.");
      return;
    }

    String customerId = readNonBlank(input, "Enter customer Id: ");

    if (!system.customerExists(customerId)) {
      System.out.println("No account found.");
      return;
    }

    String securityQuestion = null;

    for (int attempts = 1; attempts <= 3; attempts++) {
      String password = readNonBlank(input, "Enter password: ");
      securityQuestion = system.beginLogOn(customerId, password);

      if (securityQuestion != null) {
        break;
      }

      int remaining = 3 - attempts;
      if (remaining > 0) {
        System.out.println("Invalid password. Attempts remaining: " + remaining);
      } else {
        System.out.println("Maximum password attempts reached.");
      }
    }

    if (securityQuestion == null) {
      return;
    }

    System.out.println("Security Question: " + securityQuestion);
    String securityAnswer = readNonBlank(input, "Enter security answer: ");

    if (system.finishLogOn(securityAnswer)) {
      System.out.println("Welcome, " + system.getCurrentCustomer().getName() + "!");
    } else {
      System.out.println("Incorrect security answer.");
    }
  }

  private static void logOut(CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- Log Out ---");

    if (system.getCurrentCustomer() == null) {
      System.out.println("No customer is currently logged in.");
      return;
    }

    system.logOut();
    System.out.println("Logged out successfully.");
  }

  private static void browseAndSelectItems(Scanner input, CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- Product Catalog ---");

    List<Product> catalog = system.getCatalog();
    if (catalog.isEmpty()) {
      System.out.println("The catalog is empty.");
      return;
    }

    System.out.println();
    for (int i = 0; i < catalog.size(); i++) {
      Product product = catalog.get(i);
      System.out.printf(
          "%d. %s | %s | Regular: $%.2f | Sale: $%.2f%n",
          i + 1,
          product.getName(),
          product.getDescription(),
          product.getRegularPrice(),
          product.getSalePrice());
      System.out.println();
    }

    boolean selecting = true;
    while (selecting) {
      System.out.print("Enter product number to add to cart (0 to exit): ");
      int productChoice = readInt(input);

      if (productChoice == 0) {
        if (system.getShoppingCart().isEmpty()) {
          System.out.println("No products selected.");
        }
        return;
      }

      if (productChoice < 1 || productChoice > catalog.size()) {
        System.out.println("Invalid product number.");
        continue;
      }
      int quantity;
      while (true) {
        System.out.print("Enter quantity: ");
        quantity = readInt(input);
        if (quantity > 0) {
          break;
        }
        System.out.println("Quantity must be greater than 0.");
      }

      Product selectedProduct = catalog.get(productChoice - 1);
      boolean added = system.selectItem(selectedProduct.getName(), quantity);

      if (added) {
        System.out.println(selectedProduct.getName() + " added to cart.");
      } else {
        System.out.println("Item could not be added to cart.");
      }

      System.out.print("Add another product? (yes or no): ");
      String answer = input.nextLine().trim();

      if (!answer.equalsIgnoreCase("yes")) {
        selecting = false;
      }
    }
    showCart(system);
  }

  private static void showCart(CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- Shopping Cart ---");

    ShoppingCart cart = system.getShoppingCart();

    if (cart.isEmpty()) {
      System.out.println("Your cart is empty.");
      return;
    }

    for (CartItem item : cart.getItems()) {
      System.out.printf(
          "%s | Quantity: %d | Line Total: $%.2f%n",
          item.getProduct().getName(), item.getQuantity(), item.getLineTotal());
    }

    System.out.printf("Subtotal: $%.2f%n", cart.getSubtotal());
    System.out.printf("Tax: $%.2f%n", cart.getTax());
    System.out.printf("Total: $%.2f%n", cart.getTotal());
  }

  private static void makeOrder(Scanner input, CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- Make Order ---");

    if (system.getCurrentCustomer() == null) {
      System.out.println("You must log in before placing an order.");
      return;
    }

    if (system.getShoppingCart().isEmpty()) {
      System.out.println("Your cart is empty.");
      return;
    }

    showCart(system);

    System.out.println("Select delivery method: ");
    System.out.println("1. Mail ($3.00 fee)");
    System.out.println("2. In store pickup (free)");
    System.out.println("0. Cancel order");
    System.out.print("Enter choice: ");

    int choice = readInt(input);

    if (choice == 0) {
      System.out.println("Order cancelled.");
      return;
    }

    DeliveryMethod deliveryMethod;
    if (choice == 1) {
      deliveryMethod = DeliveryMethod.MAIL;
    } else if (choice == 2) {
      deliveryMethod = DeliveryMethod.IN_STORE_PICKUP;
    } else {
      System.out.println("Invalid delivery method.");
      return;
    }

    double totalWithDelivery = system.getShoppingCart().getTotal() + deliveryMethod.getFee();
    System.out.printf("Total including delivery fee: $%.2f%n", totalWithDelivery);

    Order order = system.makeOrder(deliveryMethod);

    if (order != null) {
      System.out.println("Order placed successfully.");
      printOrder(order);
      return;
    }

    System.out.println("Stored credit card was declined.");

    while (true) {
      System.out.print("Enter another 16 digit credit card number or type 0 to exit: ");
      String newCard = input.nextLine().trim();

      if (newCard.equals("0")) {
        System.out.println("Order Cancelled.");
        return;
      }

      if (!newCard.matches("\\d{16}")) {
        System.out.println("Credit card number must be exactly 16 digits.");
        continue;
      }
      order = system.makeOrderWithNewCard(deliveryMethod, newCard);

      if (order != null) {
        System.out.println("Order placed successfully with the new credit card.");
        printOrder(order);
        return;
      }

      System.out.println("That credit card was also declined.");
    }
  }

  private static void viewOrders(CustomerOrderSystem system) {
    System.out.println();
    System.out.println("--- View Orders ---");

    if (system.getCurrentCustomer() == null) {
      System.out.println("You must log in before viewing orders.");
      return;
    }

    List<Order> orders = system.viewOrders();

    if (orders.isEmpty()) {
      System.out.println("No orders found.");
      return;
    }

    for (int i = 0; i < orders.size(); i++) {
      System.out.println("Order " + (i + 1) + ":");
      printOrder(orders.get(i));
      System.out.println();
    }
  }

  private static void printOrder(Order order) {
    System.out.println("Order Date: " + order.getOrderDate());
    System.out.println("Delivery Method: " + order.getDeliveryMethod());

    for (CartItem item : order.getItems()) {
      System.out.printf(
          "%s | Quantity: %d | Line Total: $%.2f%n",
          item.getProduct().getName(), item.getQuantity(), item.getLineTotal());
    }

    System.out.printf("Subtotal: $%.2f%n", order.getSubtotal());
    System.out.printf("Tax: $%.2f%n", order.getTax());
    System.out.printf("Delivery Fee: $%.2f%n", order.getDeliveryFee());
    System.out.printf("Total: $%.2f%n", order.getTotal());
    System.out.println("Authorization Number: " + order.getAuthNumber());
  }

  private static int readInt(Scanner input) {
    while (true) {
      String value = input.nextLine().trim();

      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException e) {
        System.out.print("Invalid input: Enter a number: ");
      }
    }
  }

  private static String readNonBlank(Scanner input, String prompt) {
    while (true) {
      System.out.print(prompt);
      String value = input.nextLine().trim();

      if (!value.isEmpty()) {
        return value;
      }

      System.out.println("Input cannot be blank.");
    }
  }
}
