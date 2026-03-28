public enum DeliveryMethod {
  MAIL(3.00),
  IN_STORE_PICKUP(0.00);

  private final double fee;

  DeliveryMethod(double fee) {
    this.fee = fee;
  }

  public double getFee() {
    return fee;
  }
}
