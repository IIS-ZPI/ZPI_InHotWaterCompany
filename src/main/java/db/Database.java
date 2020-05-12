package db;

import java.sql.Connection;

import application.ProductInfo;

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
