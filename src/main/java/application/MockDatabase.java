package application;

import java.util.HashMap;

public class MockDatabase implements Database {
	private final HashMap<String, ProductInfo> map = new HashMap<>();
	
	public MockDatabase() {
		map.put("Carambola", new ProductInfo("Carambola", ProductCategory.GROCERIES, 1.23));
		map.put("Powdered water", new ProductInfo("Powdered water", ProductCategory.PREPARED_FOOD, 2.34));
		map.put("MindBooster", new ProductInfo("MindBooster", ProductCategory.PRESCRIPTION_DRUG, 3.45));
		map.put("Vitamin C", new ProductInfo("Vitamin C", ProductCategory.NONPRESCRIPTION_DRUG, 4.56));
		map.put("Iron Man suit", new ProductInfo("Iron Man suit", ProductCategory.CLOTHING, 5.67));
		map.put("Immunity for stupidity", new ProductInfo("Immunity for stupidity", ProductCategory.INTANGIBLES, 6.78));
	}
	
	@Override
	public ProductInfo get(String productName) {
		return map.get(productName);
	}

	@Override
	public ProductInfo[] getAll() {
		return map.values().toArray(new ProductInfo[map.size()]);
	}
	
	@Override
	public void store(ProductInfo productInfo) {
		map.put(productInfo.getName(), productInfo);
	}

	@Override
	public void remove(String productName) {
		map.remove(productName);
	}

	@Override
	public void clear() {
		map.clear();
	}
}
