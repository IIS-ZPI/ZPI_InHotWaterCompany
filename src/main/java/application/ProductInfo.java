package application;

public class ProductInfo {
	private final String name;
	private final ProductCategory category;
	private final double price;
	
	public ProductInfo(String name, ProductCategory category, double price) {
		this.name = name;
		this.category = category;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return name + ", " + category + ", " + price;
	}
}
