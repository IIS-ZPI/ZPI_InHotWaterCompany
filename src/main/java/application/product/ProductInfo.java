package application.product;

public class ProductInfo {
    private String product;
    private ProductCategory category;
    private double wholesalePrice;

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
