/** product in the COS */
public class Product {
  private String name;
  private String description;
  private double regularPrice;
  private double salePrice;

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
   * @return product name
   */
  public String getName() {
    return name;
  }

  /**
   * @return description of product
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return regular price of product
   */
  public double getRegularPrice() {
    return regularPrice;
  }

  /**
   * @return sale price of product
   */
  public double getSalePrice() {
    return salePrice;
  }

  /**
   * @return sale price if it is lower otherwise return regular price
   */
  public double getCurrentPrice() {
    if (salePrice < regularPrice) {
      return salePrice;
    }
    return regularPrice;
  }
}
