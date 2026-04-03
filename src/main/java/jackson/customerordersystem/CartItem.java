package jackson.customerordersystem;

/** items in cart */
public class CartItem {
  private final Product product;
  private int quantity;

  /**
   * creates a cart where items and the quantity of items are stored
   *
   * @param product product in a cart
   * @param quantity amount of a singular item you have in your cart
   */
  public CartItem(Product product, int quantity) {
    this.product = product;
    if (quantity > 0) {
      this.quantity = quantity;
    } else {
      this.quantity = 1;
    }
  }

  /**
   * returns the product in the cart item
   *
   * @return product
   */
  public Product getProduct() {
    return product;
  }

  /**
   * returns the quantity of the product
   *
   * @return quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * updates the quantity of the product
   *
   * @param quantity gets the quantity of the product
   */
  public void setQuantity(int quantity) {
    if (quantity > 0) {
      this.quantity = quantity;
    }
  }

  /**
   * calculates line total for cart item
   *
   * @return line total
   */
  public double getLineTotal() {
    return product.getCurrentPrice() * quantity;
  }
}
