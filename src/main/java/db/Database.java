package db;

import java.sql.Connection;

import application.ProductInfo;

public interface Database {
	public void initialize(String pathToSql) throws DatabaseException;
	public void insertProduct(ProductInfo productInfo) throws DatabaseException;
	public Connection getConnection();
	public void close() throws DatabaseException;
}
