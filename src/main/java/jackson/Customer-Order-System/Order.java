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

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public String getCustomerId() {
    return customerId;
  }

  public List<CartItem> getItems() {
    return new ArrayList<>(items);
  }

  public DeliveryMethod getDeliveryMethod() {
    return deliveryMethod;
  }

  public double getSubtotal() {
    return subtotal;
  }

  public double getTax() {
    return tax;
  }

  public double getDeliveryFee() {
    return deliveryFee;
  }

  public double getTotal() {
    return total;
  }

  public String getAuthNumber() {
    return authNumber;
  }
}
