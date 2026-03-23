import java.util.ArrayList;

public class Customer {
  private String customerID;
  private String password;
  private String name;
  private String address;
  private String creditCardNum;
  private String securityQuestion;
  private String securityAnswer;
  private boolean loggedIn;
  private List<Order> orders;

  public Customer(
      String customerID,
      String password,
      String name,
      String address,
      String creditCardNum,
      String securityQuestion,
      String securityAnswer,
      boolean loggedIn) {
    this.customerID = customerID;
    this.password = password;
    this.name = name;
    this.address = address;
    this.creditCardNum = creditCardNum;
    this.securityQuestion = securityQuestion;
    this.securityAnswer = securityAnswer;
    this.loggedIn = false;
    this.orders = new ArrayList<>();
  }

  public String getCustomerID() {
    return customerID;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getCreditCardNum() {
    return creditCardNum;
  }

  public String getSecurityQeustion() {
    return securityQuestion;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public boolean verifyPassword(String enteredPassword) {
    return password.equals(enteredPassword);
  }
}
