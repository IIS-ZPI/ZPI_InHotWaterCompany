package application;

import utils.CategoryResolver;

public class ProductInfo {
    private String product;
    private ProductCategory category;
    private double wholesalePrice;
    
    public ProductInfo(String product, ProductCategory category, double wholesalePrice) {
        this.product = product;
        this.category = category;
        this.wholesalePrice = wholesalePrice;
    }
    
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
    
    @Deprecated
    public String getCategoryString() {
        return CategoryResolver.getString(category);
    }
    
    public double getWholesalePrice() {
        return wholesalePrice;
    }
}
