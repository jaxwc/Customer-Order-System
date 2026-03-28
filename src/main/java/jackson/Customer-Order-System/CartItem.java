/** items in cart */
public class CartItem {
  private Product product;
  private int quantity;

  /**
   * creates a cart where items and the quantity of items are stored
   *
   * @param product product in a cart
   * @param quantity amount of a singular item you have in your cart
   */
  public CartItem(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  /**
   * @return product
   */
  public Product getProduct() {
    return product;
  }

  /**
   * @return quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * @param quantity gets the quantity of the product
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * @return line total for this cart item
   */
  public double getLineTotal() {
    return product.getCurrentPrice() * quantity;
  }
}
