package db;

import java.sql.Connection;
import java.util.List;

import application.foreignTransport.ImportCosts;
import application.foreignTransport.ImportCountry;
import application.product.ProductInfo;
import application.State;

public interface Database {
	/**
	 * Initialize database by creating tables, inserting basic data etc.
	 * Actions depends of contents of passed SQL file.
	 * Call this method only once, when creating fresh database.
	 * Don't use for other tasks (look at getConnection method) 
	 * 
	 * @param pathToSql - path to initialization SQL file
	 * @throws DatabaseException if something gone wrong
	 */
	public void initialize(String pathToSql) throws DatabaseException;
	
	/**
	 * Inserts product data to database
	 * 
	 * @param productInfo - product to insert
	 * @throws DatabaseException if something gone wrong
	 */
	public void insertProduct(ProductInfo productInfo) throws DatabaseException;
	
	/**
	 * Fetches all states from database
	 * 
	 * @return list of states
	 * @throws DatabaseException if something gone wrong
	 */
	public List<State> fetchAllStates() throws DatabaseException;
	
	/**
	 * Fetches all products from database
	 * 
	 * @return list of products
	 * @throws DatabaseException if something gone wrong
	 */
	public List<ProductInfo> fetchAllProducts() throws DatabaseException;
	
	/**
	 * Fetches all category names from database
	 * 
	 * @return list of category names
	 * @throws DatabaseException if something gone wrong
	 */
	public List<String> fetchAllCategoryNames() throws DatabaseException;
	
	/**
	 * Updates logistic costs for specified state
	 * 
	 * @param stateName - state name to update
	 * @param logisticCost - new logistic costs for state
	 * @throws DatabaseException if something gone wrong
	 */
	public void updateLogisticCost(String stateName, double logisticCost) throws DatabaseException;
	
	/**
	 * Inserts import country to database
	 * 
	 * @param importCountry - import country to insert
	 * @throws DatabaseException if something gone wrong
	 */
	public void insertImportCountry(ImportCountry importCountry) throws DatabaseException;
	
	/**
	 * Updates import costs for specified import country
	 * 
	 * @param countryName - country name to update
	 * @param importCosts - new import costs for country
	 * @throws DatabaseException if something gone wrong
	 */
	public void updateImportCosts(String countryName, ImportCosts importCosts) throws DatabaseException;
	
	/**
	 * Fetches all import countries from database
	 * 
	 * @return list of import countries
	 * @throws DatabaseException if something gone wrong
	 */
	public List<ImportCountry> fetchAllImportCountries() throws DatabaseException;
	
	/**
	 * Returns Connection object to perform various tasks not provided by this interface
	 * 
	 * @return Connection object
	 */
	public Connection getConnection();
	
	/**
	 * Closes connection to database
	 * 
	 * @throws DatabaseException if something gone wrong
	 */
	public void close() throws DatabaseException;
}