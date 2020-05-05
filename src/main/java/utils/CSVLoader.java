package utils;

import application.ProductCategory;
import application.ProductInfo;
import db.Database;

public abstract class CSVLoader {
	public static void load(CSV csv, Database db) throws Exception {
		String[] row = csv.readRow();
		
		while (row != null) {
			String name = row[0];
			ProductCategory category = CategoryResolver.getEnum(row[1]);
			double price = Double.parseDouble(row[2]);
			
			db.insertProduct(new ProductInfo(name, category, price));
			row = csv.readRow();
		}
	}
}
