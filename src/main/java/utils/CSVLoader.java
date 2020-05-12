package utils;

import java.io.IOException;

import application.ProductCategory;
import application.ProductInfo;
import db.Database;
import db.DatabaseException;

public abstract class CSVLoader {
	private static ProductCategory resolveCategory(String categoryString) {
		switch (categoryString) {
			case "groceries":				return ProductCategory.GROCERIES;
			case "Prepared-food":			return ProductCategory.PREPARED_FOOD;
			case "Prescription-drug":		return ProductCategory.PRESCRIPTION_DRUG;
			case "Non-prescription-drug":	return ProductCategory.NONPRESCRIPTION_DRUG;
			case "Clothing":				return ProductCategory.CLOTHING;
			case "Intangibles":				return ProductCategory.INTANGIBLES;
			default:						return ProductCategory.UNDEFINED;
		}
	}
	
	/**
	 * Loads contents of CSV file to database
	 * 
	 * @param csv - file to read
	 * @param db - database to write
	 * @throws IOException if reading from csv gone wrong
	 * @throws DatabaseException if writing to database gone wrong
	 */
	public static void load(CSV csv, Database db) throws IOException, DatabaseException {
		csv.revert();
		String[] row = csv.readRow();
		
		while (row != null) {
			String name = row[0];
			ProductCategory category = resolveCategory(row[1]);
			double price = Double.parseDouble(row[2]);
			
			db.insertProduct(new ProductInfo(name, category, price));
			
			row = csv.readRow();
		}
	}
}
