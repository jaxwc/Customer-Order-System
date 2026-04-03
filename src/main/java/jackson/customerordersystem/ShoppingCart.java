package jackson.customerordersystem;

import java.util.ArrayList;
import java.util.List;

/** tax rate and total price of shopping cart */
public class ShoppingCart {
  private static final double TAX_RATE = .0825;
  private final List<CartItem> items;

  /** creates an empty shopping cart */
  public ShoppingCart() {
    items = new ArrayList<>();
  }

  /**
   * adds a products and the quantity of the product to the shopping cart
   *
   * @param product selected product
   * @param quantity quantity selected
   */
  public void addProduct(Product product, int quantity) {
    if (product == null || quantity <= 0) {
      return;
    }
    for (CartItem item : items) {
      if (item.getProduct().getName().equalsIgnoreCase(product.getName())) {
        item.setQuantity(item.getQuantity() + quantity);
        return;
      }
    }
    items.add(new CartItem(product, quantity));
  }

  /**
   * returns the items in the shopping cart
   *
   * @return copy of cart items
   */
  public List<CartItem> getItems() {
    return new ArrayList<>(items);
  }

  /**
   * calculates subtotal
   *
   * @return subtotal of all items in cart
   */
  public double getSubtotal() {
    double subtotal = 0.0;

    for (CartItem item : items) {
      subtotal += item.getLineTotal();
    }
    return subtotal;
  }

  /**
   * calculate the tax for the cart
   *
   * @return tax
   */
  public double getTax() {
    return getSubtotal() * TAX_RATE;
  }

  /**
   * calculates total price including tax
   *
   * @return total price of cart
   */
  public double getTotal() {
    return getSubtotal() + getTax();
  }

  /**
   * checks whether the cart has any items
   *
   * @return true if the cart is empty otherwise return false
   */
  public boolean isEmpty() {
    return items.isEmpty();
  }

  /** clears items from cart */
  public void clear() {
    items.clear();
  }
}
