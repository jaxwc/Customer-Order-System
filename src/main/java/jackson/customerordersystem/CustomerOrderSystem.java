package jackson.customerordersystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** main system class to put the customer order system together */
public class CustomerOrderSystem {
  private static final int MAX_LOGIN_ATTEMPTS = 3;

  private final List<Customer> customers;
  private final List<Product> catalog;
  private final List<String> securityQuestions;
  private final ShoppingCart shoppingCart;
  private final Bank bank;
  private Customer currentCustomer;
  private Customer pendingLoginCustomer;
  private int failedLoginAttempts;

  /** create the customer order system */
  public CustomerOrderSystem() {
    customers = new ArrayList<>();
    catalog = new ArrayList<>();
    securityQuestions = new ArrayList<>();
    shoppingCart = new ShoppingCart();
    currentCustomer = null;
    pendingLoginCustomer = null;
    bank = new Bank();
    failedLoginAttempts = 0;

    securityQuestions.add("What is your favorite color?");
    securityQuestions.add("What city were you born in?");
    securityQuestions.add("What was the name of your first pet?");
  }

  /**
   * returns available security questions
   *
   * @return copy of security questions
   */
  public List<String> getSecurityQuestions() {
    return new ArrayList<>(securityQuestions);
  }

  /**
   * adds product to the catalog
   *
   * @param product add product
   */
  public void addProductToCatalog(Product product) {
    if (product != null) {
      catalog.add(product);
    }
  }

  /**
   * returns product catalog
   *
   * @return copy of catalog
   */
  public List<Product> getCatalog() {
    return new ArrayList<>(catalog);
  }

  /**
   * checks if a customer id is available
   *
   * @return true if id is available otherwise return false
   */
  public boolean isCustomerIdAvailable(String customerId) {
    if (customerId == null || customerId.trim().isEmpty()) {
      return false;
    }
    return findCustomerById(customerId) == null;
  }

  /**
   * checks if the password follows the rules
   *
   * @param password password to check
   * @return true if password is valid otherwise return false
   */
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

  /**
   * creates new customer account
   *
   * @param customerId customer id
   * @param password customer password
   * @param name customer name
   * @param address customer address
   * @param creditCard customer credit card
   * @param securityQuestion selected security question
   * @param securityAnswer answer to the security question
   * @return true if account is created otherwise return false
   */
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

    if (!bank.isValidCreditCard(creditCard)) {
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

  /**
   * attempts to log a customer in to the system, verifies customer id and password
   *
   * @param customerId entered customer id
   * @param password entered customer password
   * @return customers security question of credentials are valid otherwise returns null if invalid
   *     or are locked out
   */
  public String beginLogOn(String customerId, String password) {
    Customer customer = findCustomerById(customerId);

    if (customer == null) {
      pendingLoginCustomer = null;
      return null;
    }

    if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
      pendingLoginCustomer = null;
      return null;
    }

    if (!customer.verifyPassword(password)) {
      failedLoginAttempts++;
      pendingLoginCustomer = null;
      return null;
    }

    pendingLoginCustomer = customer;
    return customer.getSecurityQuestion();
  }

  /**
   * verifies security answer and logs the customer in
   *
   * @param securityAnswer entered answer to the security question
   * @return true if the security answer is correct and logs in other wise returns false
   */
  public boolean finishLogOn(String securityAnswer) {

    if (pendingLoginCustomer == null) {
      return false;
    }

    if (!pendingLoginCustomer.verifySecurityAnswer(securityAnswer)) {
      pendingLoginCustomer = null;
      return false;
    }

    pendingLoginCustomer.logIn();
    currentCustomer = pendingLoginCustomer;
    pendingLoginCustomer = null;
    failedLoginAttempts = 0;
    return true;
  }

  /** logs out the current customer */
  public void logOut() {
    if (currentCustomer != null) {
      currentCustomer.logOut();
      currentCustomer = null;
    }
    pendingLoginCustomer = null;
    failedLoginAttempts = 0;
  }

  /**
   * adds the selected product and the quantity of the product to the cart
   *
   * @param productName selected product name
   * @param quantity quantity of the product
   * @return true if the item is added otherwise return false
   */
  public boolean selectItem(String productName, int quantity) {
    Product product = findProductByName(productName);

    if (product == null || quantity <= 0) {
      return false;
    }
    shoppingCart.addProduct(product, quantity);
    return true;
  }

  /**
   * places an order using the customers credit card
   *
   * @param deliveryMethod customers selected delivery method
   * @return complete order if it is successful otherwise return null
   */
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

  /**
   * places an order with customers new credit card if there was a failed charge
   *
   * @param deliveryMethod selected delivery method
   * @param newCreditCard customer new credit card number
   * @return complete order if it sucessful otherwise null
   */
  public Order makeOrderWithNewCard(DeliveryMethod deliveryMethod, String newCreditCard) {
    if (currentCustomer == null || !currentCustomer.isLoggedIn()) {
      return null;
    }
    if (shoppingCart.isEmpty() || deliveryMethod == null) {
      return null;
    }
    if (!bank.isValidCreditCard(newCreditCard)) {
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

  /**
   * returns the current customers orders
   *
   * @return order history for the customer that is logged in or an empty list
   */
  public List<Order> viewOrders() {
    if (currentCustomer == null || !currentCustomer.isLoggedIn()) {
      return new ArrayList<>();
    }
    return currentCustomer.getOrders();
  }

  /**
   * returns the customers current shopping cart
   *
   * @return customers current shopping cart
   */
  public ShoppingCart getShoppingCart() {
    return shoppingCart;
  }

  /**
   * returns the current customer that is logged in
   *
   * @return current customer
   */
  public Customer getCurrentCustomer() {
    return currentCustomer;
  }

  /**
   * finds a customer by the customer id
   *
   * @param customerId customer id to search for
   * @return matching customer to customer id or null if not found
   */
  private Customer findCustomerById(String customerId) {
    for (Customer customer : customers) {
      if (customer.getCustomerId().equalsIgnoreCase(customerId)) {
        return customer;
      }
    }
    return null;
  }

  /**
   * finds a product by product name
   *
   * @param productName product name to search for
   * @return matching product or return null if not found
   */
  private Product findProductByName(String productName) {
    for (Product product : catalog) {
      if (product.getName().equalsIgnoreCase(productName)) {
        return product;
      }
    }
    return null;
  }
}
