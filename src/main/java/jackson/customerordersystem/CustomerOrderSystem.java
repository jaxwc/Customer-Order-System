package jackson.customerordersystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrderSystem {
  private static final int MAX_LOGIN_ATTEMPTS = 3;

  private List<Customer> customers;
  private List<Product> catalog;
  private List<String> securityQuestions;
  private ShoppingCart shoppingCart;
  private Customer currentCustomer;
  private Bank bank;
  private int failedLoginAttempts;
  private String lastLoginCustomerId;

  public CustomerOrderSystem() {
    customers = new ArrayList<>();
    catalog = new ArrayList<>();
    securityQuestions = new ArrayList<>();
    shoppingCart = new ShoppingCart();
    currentCustomer = null;
    bank = new Bank();
    failedLoginAttempts = 0;
    lastLoginCustomerId = null;

    securityQuestions.add("What is your favorite color?");
    securityQuestions.add("What city were you born in?");
    securityQuestions.add("What was the name of your first pet?");
  }

  public List<String> getSecurityQuestions() {
    return new ArrayList<>(securityQuestions);
  }

  public void addProductToCatalog(Product product) {
    if (product != null) {
      catalog.add(product);
    }
  }

  public List<Product> getCatalog() {
    return new ArrayList<>(catalog);
  }

  public boolean isCustomerIdAvailable(String customerId) {
    if (customerId == null || customerId.trim().isEmpty()) {
      return false;
    }
    return findCustomerById(customerId) == null;
  }

  public boolean isValidPassword(String password) {
    if (password == null || password.length() < 6) {
      return false;
    }

    boolean hasDigit = false;
    boolean hasUpperCase = false;
    boolean hasSpecial = false;

    for (int i = 0; i < password.length(); i++) {
      char ch = password.charAt(i);

      if (Character.isDigit(ch)) {
        hasDigit = true;
      } else if (Character.isUpperCase(ch)) {
        hasUpperCase = true;
      } else if ("@#$%&*".indexOf(ch) >= 0) {
        hasSpecial = true;
      }
    }
    return hasDigit && hasUpperCase && hasSpecial;
  }

  public boolean createAccount(
      String customerId,
      String password,
      String name,
      String address,
      String creditCard,
      String securityQuestion,
      String securityAnswer) {
    if (!isCustomerIdAvailable(customerId)) {
      return false;
    }
    if (!isValidPassword(password)) {
      return false;
    }
    if (name == null || name.trim().isEmpty()) {
      return false;
    }

    if (address == null || address.trim().isEmpty()) {
      return false;
    }

    if (creditCard == null || creditCard.trim().isEmpty()) {
      return false;
    }

    if (securityQuestion == null || !securityQuestions.contains(securityQuestion)) {
      return false;
    }
    if (securityAnswer == null || securityAnswer.trim().isEmpty()) {
      return false;
    }

    Customer customer =
        new Customer(
            customerId, password, name, address, creditCard, securityQuestion, securityAnswer);
    customers.add(customer);
    return true;
  }

  public boolean logOn(String customerId, String password, String securityAnswer) {
    Customer customer = findCustomerById(customerId);

    if (customer == null) {
      return false;
    }

    if (lastLoginCustomerId == null || !lastLoginCustomerId.equalsIgnoreCase(customerId)) {
      failedLoginAttempts = 0;
      lastLoginCustomerId = customerId;
    }

    if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
      return false;
    }

    if (!customer.verifyPassword(password)) {
      failedLoginAttempts++;
      return false;
    }
    if (!customer.verifySecurityAnswer(securityAnswer)) {
      failedLoginAttempts = 0;
      lastLoginCustomerId = null;
      return false;
    }

    customer.logIn();
    currentCustomer = customer;
    failedLoginAttempts = 0;
    lastLoginCustomerId = null;
    return true;
  }

  public void logOut() {
    if (currentCustomer != null) {
      currentCustomer.logOut();
      currentCustomer = null;
    }
  }

  public boolean selectItem(String productName, int quantity) {
    Product product = findProductByName(productName);

    if (product == null || quantity <= 0) {
      return false;
    }
    shoppingCart.addProduct(product, quantity);
    return true;
  }

  public Order makeOrder(DeliveryMethod deliveryMethod) {
    if (currentCustomer == null || !currentCustomer.isLoggedIn()) {
      return null;
    }

    if (shoppingCart.isEmpty() || deliveryMethod == null) {
      return null;
    }
    double subtotal = shoppingCart.getSubtotal();
    double tax = shoppingCart.getTax();
    double deliveryFee = deliveryMethod.getFee();
    double total = shoppingCart.getTotal() + deliveryFee;

    String authNumber = bank.charge(currentCustomer.getCreditCard(), total);
    if (authNumber == null) {
      return null;
    }
    Order order =
        new Order(
            LocalDate.now(),
            currentCustomer.getCustomerId(),
            shoppingCart.getItems(),
            deliveryMethod,
            subtotal,
            tax,
            deliveryFee,
            total,
            authNumber);
    currentCustomer.addOrder(order);
    shoppingCart.clear();
    return order;
  }

  public Order makeOrderWithNewCard(DeliveryMethod deliveryMethod, String newCreditCard) {
    if (currentCustomer == null || !currentCustomer.isLoggedIn()) {
      return null;
    }
    if (shoppingCart.isEmpty() || deliveryMethod == null) {
      return null;
    }

    if (newCreditCard == null || newCreditCard.trim().isEmpty()) {
      return null;
    }
    double subtotal = shoppingCart.getSubtotal();
    double tax = shoppingCart.getTax();
    double deliveryFee = deliveryMethod.getFee();
    double total = shoppingCart.getTotal() + deliveryFee;

    String authNumber = bank.charge(newCreditCard, total);

    if (authNumber == null) {
      return null;
    }

    currentCustomer.updateCreditCard(newCreditCard);

    Order order =
        new Order(
            LocalDate.now(),
            currentCustomer.getCustomerId(),
            shoppingCart.getItems(),
            deliveryMethod,
            subtotal,
            tax,
            deliveryFee,
            total,
            authNumber);
    currentCustomer.addOrder(order);
    shoppingCart.clear();
    return order;
  }

  public List<Order> viewOrders() {
    if (currentCustomer == null || !currentCustomer.isLoggedIn()) {
      return new ArrayList<>();
    }
    return currentCustomer.getOrders();
  }

  public ShoppingCart getShoppingCart() {
    return shoppingCart;
  }

  public Customer getCurrentCustomer() {
    return currentCustomer;
  }

  private Customer findCustomerById(String customerId) {
    for (Customer customer : customers) {
      if (customer.getCustomerId().equalsIgnoreCase(customerId)) {
        return customer;
      }
    }
    return null;
  }

  private Product findProductByName(String productName) {
    for (Product product : catalog) {
      if (product.getName().equalsIgnoreCase(productName)) {
        return product;
      }
    }
    return null;
  }
}
