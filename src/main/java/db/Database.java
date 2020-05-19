package db;

import java.sql.Connection;
import java.util.List;

import application.ProductInfo;
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
	 * @return - list of states
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
