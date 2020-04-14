package application;

public interface Database {
	public ProductInfo get(String productName);
	public ProductInfo[] getAll();
	public void store(ProductInfo productInfo);
	public void remove(String productName);
	public void clear();
}
