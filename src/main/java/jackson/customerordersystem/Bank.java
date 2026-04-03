package jackson.customerordersystem;

import java.util.Random;

/** bank is used for charging customer orders */
public class Bank {
  private static final double CREDIT_LIMIT = 5000.00;
  private final Random random;

  /** creates a bank */
  public Bank() {
    random = new Random();
  }

  /**
   * charges the credit card for the amount of the order
   *
   * @param creditCard customers credit card number
   * @param amount amount of the customers order
   * @return four digit auth number if approved, otherwise will return null
   */
  public String charge(String creditCard, double amount) {
    if (isApproved(creditCard, amount)) {
      return generateAuthNumber();
    }
    return null;
  }

  /**
   * checks if a charge is approved
   *
   * @param creditCard customers credit card number
   * @param amount amount of the customers order
   * @return true if the charge is approved other wise return false
   */
  private boolean isApproved(String creditCard, double amount) {
    if (creditCard == null) {
      return false;
    }
    String trimmedCard = creditCard.trim();

    if (trimmedCard.length() != 16) {
      return false;
    }

    for (int i = 0; i < trimmedCard.length(); i++) {
      if (!Character.isDigit(trimmedCard.charAt(i))) {
        return false;
      }
    }
    return amount > 0 && amount <= CREDIT_LIMIT;
  }

  /**
   * generates a four digit auth Number
   *
   * @return a four digit auth number
   */
  private String generateAuthNumber() {
    int number = random.nextInt(10000);
    return String.format("%04d", number);
  }
}
