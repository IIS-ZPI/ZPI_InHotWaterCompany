package utils;

import java.io.IOException;

import application.ImportCosts;
import application.ImportCountry;
import application.ProductCategory;
import application.ProductInfo;
import db.Database;
import db.DatabaseException;

public final class CSVLoader {
	private CSVLoader() {
		throw new IllegalStateException("Utility class");
	}
	
	private static ProductCategory resolveProductCategory(String categoryString) {
		switch (categoryString) {
			case "groceries":                return ProductCategory.GROCERIES;
			case "Prepared-food":            return ProductCategory.PREPARED_FOOD;
			case "Prescription-drug":        return ProductCategory.PRESCRIPTION_DRUG;
			case "Non-prescription-drug":    return ProductCategory.NONPRESCRIPTION_DRUG;
			case "Clothing":                 return ProductCategory.CLOTHING;
			case "Intangibles":              return ProductCategory.INTANGIBLES;
			default:                         return ProductCategory.UNDEFINED;
		}
	}
	
	/**
	 * Loads contents of CSV file with products to database
	 * 
	 * @param csv - file to read
	 * @param database - database to write
	 * @throws IOException if reading from csv gone wrong
	 * @throws DatabaseException if writing to database gone wrong
	 */
	public static void loadProducts(CSV csv, Database database) throws IOException, DatabaseException {
		csv.revert();
		String[] row = csv.readRow();
		
		while (row != null) {
			String name = row[0];
			ProductCategory category = resolveProductCategory(row[1]);
			double price = Double.parseDouble(row[2]);
			
			database.insertProduct(new ProductInfo(name, category, price));
			
			row = csv.readRow();
		}
	}
	
	/**
	 * Load contents of CSV file with logistic costs to database
	 * 
	 * @param csv - file to read
	 * @param database - database to write
	 * @throws IOException if reading from csv gone wrong
	 * @throws DatabaseException if writing to database gone wrong
	 */
	public static void loadLogisticCosts(CSV csv, Database database) throws IOException, DatabaseException {
		csv.revert();
		String[] row = csv.readRow();
		
		while (row != null) {
			String stateName = row[0];
//			String stateCode = row[1];  // unused
			double transportFee = Double.parseDouble(row[2]);
			
			database.updateLogisticCost(stateName, transportFee);
			
			row = csv.readRow();
		}
	}
	
	/**
	 * Load contents of CSV file with international transportation costs to database
	 * 
	 * @param csv - file to read
	 * @param database - database to write
	 * @throws IOException if reading from csv gone wrong
	 * @throws DatabaseException if writing to database gone wrong
	 */
	public static void loadInternationalTransportationCosts(CSV csv, Database database) throws IOException, DatabaseException {
		csv.revert();
		String[] row = csv.readRow();
		
		while (row != null) {
			String countryName = row[0];
			String countryCode = row[1];
			String currency = row[2];
			double transportFee = Double.parseDouble(row[3]);
			double consumablesImportTariff = Double.parseDouble(row[4]);
			double othersImportTariff = Double.parseDouble(row[5]);
			
			ImportCosts importCosts = new ImportCosts(transportFee, consumablesImportTariff, othersImportTariff);
			database.insertImportCountry(new ImportCountry(countryName, countryCode, currency, importCosts));
			
			row = csv.readRow();
		}
	}
}
