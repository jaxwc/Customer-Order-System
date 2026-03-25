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
  public double getQuantity() {
    return quantity;
  }

  /**
   * @param quantity gets the quantity of the product
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * @return the quantity total
   */
  public double getQuantityTotal() {
    return product.getCurrentPrice() * quantity;
  }
}
