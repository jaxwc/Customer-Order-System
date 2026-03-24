import java.util.ArrayList;
import java.util.List;

/** customers account in the COS */
public class Customer {
  private String customerID;
  private String password;
  private String name;
  private String address;
  private String creditCard;
  private String securityQuestion;
  private String securityAnswer;
  private boolean loggedIn;
  private List<Order> orders;

  /**
   * creates a customer with account and profile information
   *
   * @param customerID unique customer ID
   * @param password customers password
   * @param name customers name
   * @param address customers address
   * @param creditCard customers credit card number
   * @param securityQuestion selected security question
   * @param securityAnswer answer to security question
   */
  public Customer(
      String customerID,
      String password,
      String name,
      String address,
      String creditCard,
      String securityQuestion,
      String securityAnswer) {
    this.customerID = customerID;
    this.password = password;
    this.name = name;
    this.address = address;
    this.creditCard = creditCard;
    this.securityQuestion = securityQuestion;
    this.securityAnswer = securityAnswer;
    this.loggedIn = false;
    this.orders = new ArrayList<>();
  }

  /**
   * @return customer ID
   */
  public String getCustomerID() {
    return customerID;
  }

  /**
   * @return customers name
   */
  public String getName() {
    return name;
  }

  /**
   * @return customers address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @return customers credit card number
   */
  public String getCreditCard() {
    return creditCard;
  }

  /**
   * @return customers security question
   */
  public String getSecurityQuestion() {
    return securityQuestion;
  }

  /**
   * @return return true if the customer is logged in otherwise return false
   */
  public boolean isLoggedIn() {
    return loggedIn;
  }

  /**
   * checks the entered password to see if it matches the stored password
   *
   * @param enteredPassword the password entered by the customer
   * @return return true if the password matches otherwise return false
   */
  public boolean verifyPassword(String enteredPassword) {
    return password.equals(enteredPassword);
  }

  /**
   * checks the entered answer for the security question to see if it matches the stored answer
   *
   * @param enteredAnswer the answer to the security question entered by the customer
   * @return return true if the answer matches otherwise return false
   */
  public boolean verifySecurityAnswer(String enteredAnswer) {
    return securityAnswer.equalsIgnoreCase(enteredAnswer);
  }

  /** customer is logged in */
  public void logIn() {
    loggedIn = true;
  }

  /** customer is logged out */
  public void logOut() {
    loggedIn = false;
  }

  /**
   * puts a new order in the customers order list
   *
   * @param order the order to add
   */
  public void addOrder(Order order) {
    if (order != null) {
      orders.add(order);
    }
  }

  /**
   * returns the customers order list
   *
   * @return returns the customers order
   */
  public List<Order> getOrders() {
    return new ArrayList<>(orders);
  }

  /**
   * updates customers credit card Number
   *
   * @param newCard the new credit card number
   */
  public void updateCreditCard(String newCard) {
    if (newCard != null && !newCard.trim().isEmpty()) {
      creditCard = newCard;
    }
  }
}
