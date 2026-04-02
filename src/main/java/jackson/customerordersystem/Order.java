package jackson.customerordersystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** complete customer order */
public class Order {
  private LocalDate orderDate;
  private String customerId;
  private List<CartItem> items;
  private DeliveryMethod deliveryMethod;
  private double subtotal;
  private double tax;
  private double deliveryFee;
  private double total;
  private String authNumber;

  /**
   * creates a complete order with all items in the order
   *
   * @param orderDate date of the order
   * @param customerId id of the customer who made the order
   * @param items all items included in the order
   * @param deliveryMethod the selected delivery method
   * @param subtotal subtotal before tax and delivery fee
   * @param tax tax amount
   * @param deliveryFee delivery fee
   * @param total total of the order
   * @param authNumber bank authorization number
   */
  public Order(
      LocalDate orderDate,
      String customerId,
      List<CartItem> items,
      DeliveryMethod deliveryMethod,
      double subtotal,
      double tax,
      double deliveryFee,
      double total,
      String authNumber) {
    this.orderDate = orderDate;
    this.customerId = customerId;
    this.items = new ArrayList<>(items);
    this.deliveryMethod = deliveryMethod;
    this.subtotal = subtotal;
    this.tax = tax;
    this.deliveryFee = deliveryFee;
    this.total = total;
    this.authNumber = authNumber;
  }

  /**
   * @return order date
   */
  public LocalDate getOrderDate() {
    return orderDate;
  }

  /**
   * @return customer id
   */
  public String getCustomerId() {
    return customerId;
  }

  /**
   * @return copy of items
   */
  public List<CartItem> getItems() {
    return new ArrayList<>(items);
  }

  /**
   * @return delivery method selected
   */
  public DeliveryMethod getDeliveryMethod() {
    return deliveryMethod;
  }

  /**
   * @return order subtotal
   */
  public double getSubtotal() {
    return subtotal;
  }

  /**
   * @return tax amount
   */
  public double getTax() {
    return tax;
  }

  /**
   * @return delivery fee
   */
  public double getDeliveryFee() {
    return deliveryFee;
  }

  /**
   * @return order total
   */
  public double getTotal() {
    return total;
  }

  /**
   * @return bank auth number
   */
  public String getAuthNumber() {
    return authNumber;
  }
}
