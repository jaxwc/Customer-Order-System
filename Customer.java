public class Customer {
  private String customerID;
  private String password;
  private String name;
  private String address;
  private String creditCardNum;
  private String securityQuestion;
  private String securityAnswer;
  private boolean loggedIn;

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
    this.loggedIn = loggedIn;
  }
}
