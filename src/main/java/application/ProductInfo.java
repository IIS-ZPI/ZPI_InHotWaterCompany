package application;

import utils.CategoryResolver;

public class ProductInfo {

    private final String product;
    private final ProductCategory category;
    private final double wholesalePrice;

    public ProductInfo(String product, ProductCategory category, double wholesalePrice) {
        this.product = product;
        this.category = category;
        this.wholesalePrice = wholesalePrice;
    }

    /**
     * @deprecated (15.06.2020 old method)
     */
    @Deprecated
    public ProductInfo(String product, String category, double wholesalePrice) {
        this.product = product;
        this.category = CategoryResolver.getEnum(category);
        this.wholesalePrice = wholesalePrice;
    }

    public String getProduct() {
        return product;
    }

    public ProductCategory getCategory() {
        return category;
    }

    /**
     * @deprecated (15.06.2020 old method)
     */
    @Deprecated
    public String getCategoryString() {
        return CategoryResolver.getString(category);
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }
}
