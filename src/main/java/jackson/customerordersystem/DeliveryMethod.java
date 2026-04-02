package jackson.customerordersystem;

/** delivery options for customer orders */
public enum DeliveryMethod {
  MAIL(3.00),
  IN_STORE_PICKUP(0.00);

  private final double fee;

  /**
   * creates a delivery method with the fee
   *
   * @param fee delivery fee
   */
  DeliveryMethod(double fee) {
    this.fee = fee;
  }

  /**
   * @return delivery fee
   */
  public double getFee() {
    return fee;
  }
}
