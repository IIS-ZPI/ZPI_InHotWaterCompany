package application;

public class ProductInfo {

    private final String product;
    private final ProductCategory category;
    private final double wholesalePrice;

    public ProductInfo(String product, ProductCategory category, double wholesalePrice) {
        this.product = product;
        this.category = category;
        this.wholesalePrice = wholesalePrice;
    }

    public String getProduct() {
        return product;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }
}
