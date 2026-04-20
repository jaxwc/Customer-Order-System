package jackson.customerordersystem;

/** product in the customer order system */
public class Product {
  private final String name;
  private final String description;
  private final double regularPrice;
  private final double salePrice;

  /**
   * creates a product with product information
   *
   * @param name name of product
   * @param description description of product
   * @param regularPrice regular price of product
   * @param salePrice sale price of product
   */
  public Product(String name, String description, double regularPrice, double salePrice) {
    this.name = name;
    this.description = description;
    this.regularPrice = regularPrice;
    this.salePrice = salePrice;
  }

  /**
   * returns the product name
   *
   * @return product name
   */
  public String getName() {
    return name;
  }

  /**
   * returns product description
   *
   * @return product description
   */
  public String getDescription() {
    return description;
  }

  /**
   * returns regular price of the product
   *
   * @return regular price
   */
  public double getRegularPrice() {
    return regularPrice;
  }

  /**
   * returns sale price of product
   *
   * @return sale price
   */
  public double getSalePrice() {
    return salePrice;
  }

  /**
   * returns the price the system will you use for the product
   *
   * @return sale price if it is lower otherwise return regular price
   */
  public double getCurrentPrice() {
    if (salePrice < regularPrice) {
      return salePrice;
    }
    return regularPrice;
  }
}
