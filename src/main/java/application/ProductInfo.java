package application;

public class ProductInfo {
    private String product;
    private String category;
    private double wholesalePrice;

    public ProductInfo(String product, String category, double wholesalePrice) {
        this.product = product;
        this.category = category;
        this.wholesalePrice = wholesalePrice;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }
}
